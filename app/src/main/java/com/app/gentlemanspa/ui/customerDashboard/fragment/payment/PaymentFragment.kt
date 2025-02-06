package com.app.gentlemanspa.ui.customerDashboard.fragment.payment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.app.gentlemanspa.R
import com.app.gentlemanspa.base.MyApplication.Companion.hideProgress
import com.app.gentlemanspa.base.MyApplication.Companion.showProgress
import com.app.gentlemanspa.databinding.FragmentPaymentBinding
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.network.Status
import com.app.gentlemanspa.ui.customerDashboard.activity.CustomerActivity
import com.app.gentlemanspa.ui.customerDashboard.fragment.cart.CartFragmentDirections
import com.app.gentlemanspa.ui.customerDashboard.fragment.cart.model.CustomerPlaceOrderRequest
import com.app.gentlemanspa.ui.customerDashboard.fragment.cart.viewModel.CartViewModel
import com.app.gentlemanspa.ui.customerDashboard.fragment.payment.viewModel.PaymentViewModel
import com.app.gentlemanspa.utils.AlertWithoutCancelCallbackInt
import com.app.gentlemanspa.utils.ViewModelFactory
import com.app.gentlemanspa.utils.openUrlInBrowser
import com.app.gentlemanspa.utils.setGone
import com.app.gentlemanspa.utils.setVisible
import com.app.gentlemanspa.utils.showAlertForPlaceOrder
import com.app.gentlemanspa.utils.showToast


class PaymentFragment : Fragment(), View.OnClickListener {
    lateinit var binding: FragmentPaymentBinding
    val args: PaymentFragmentArgs by navArgs()
    private val viewModel: PaymentViewModel by viewModels {
        ViewModelFactory(
            InitialRepository()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObserver()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!this::binding.isInitialized) {
            binding = FragmentPaymentBinding.inflate(layoutInflater, container, false)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        (activity as CustomerActivity).bottomNavigation(false)
        binding.onClick = this
        if (args.sessionUrl.isNotBlank()) {
            loadUrlInWebView(args.sessionUrl)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadUrlInWebView(url: String) {
        val webSettings = binding.wvPayment.settings
        webSettings.javaScriptEnabled = true
        binding.wvPayment.webViewClient = WebViewClient()
        binding.wvPayment.webChromeClient = WebChromeClient()
        // Set WebViewClient to handle page navigation within the WebView
        binding.wvPayment.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                val url = request?.url.toString()
                Log.d("data", "Loading URL:$url")
                if (url.contains("OrderConfirmation?paymentId")) {
                    paymentIdAPI()
                    return true
                } else if (url.contains("cancelpay.com")) {
                   findNavController().popBackStack()
                    return true

                }else if(url.contains("privacy") || url.contains("legal")){
                    openUrlInBrowser(requireContext(),url)
                    return true
                } else {
                    return false
                }
            }
        }
        binding.wvPayment.loadUrl(url)
    }

    private fun paymentIdAPI() {
        viewModel.paymentId.set(args.paymentId)
        viewModel.orderConfirmationApi()
    }

    override fun onClick(v: View) {
        when (v) {
            binding.ivArrowBack -> {
                findNavController().popBackStack()
            }
        }
    }

    @SuppressLint("SetTextI18n", "DefaultLocale")
    private fun initObserver() {
        viewModel.resultOrderConfirmation.observe(this) {
            it.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        showProgress(requireContext())

                    }

                    Status.SUCCESS -> {
                        hideProgress()
                        Log.d("payment", "resultOrderConfirmation data->${it.data?.data}")
                        if (it.data?.data?.paymentStatus == "Paid") {
                            val request =
                                CustomerPlaceOrderRequest(0, "AtVenue", "Online", args.paymentId)
                            viewModel.customerPlaceOrderApi(request)
                        } else {
                            requireContext().showToast("Payment Failed")
                        }

                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        hideProgress()
                    }
                }
            }
        }
        viewModel.resultCustomerPlaceOrder.observe(this) {
            it.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        showProgress(requireContext())

                    }

                    Status.SUCCESS -> {
                        hideProgress()
                        Log.d("payment", "resultCustomerPlaceOrder data->${it.data?.data}")
                        requireContext().showAlertForPlaceOrder(it.message.toString(), object :
                            AlertWithoutCancelCallbackInt {
                            override fun onOkayClicked(view: View) {
                                val action =
                                    PaymentFragmentDirections.actionPaymentFragmentToHomeCustomerFragment()
                                findNavController().navigate(action)
                            }
                        })

                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        hideProgress()
                    }
                }
            }
        }
    }
}