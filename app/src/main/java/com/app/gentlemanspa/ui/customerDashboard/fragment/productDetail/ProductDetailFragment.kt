package com.app.gentlemanspa.ui.customerDashboard.fragment.productDetail

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.app.gentlemanspa.R
import com.app.gentlemanspa.base.MyApplication
import com.app.gentlemanspa.base.MyApplication.Companion.hideProgress
import com.app.gentlemanspa.base.MyApplication.Companion.showProgress
import com.app.gentlemanspa.databinding.FragmentProductDetailBinding
import com.app.gentlemanspa.network.ApiConstants
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.network.Status
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.viewModel.HomeCustomerViewModel
import com.app.gentlemanspa.ui.customerDashboard.fragment.productDetail.viewModel.ProductDetailViewModel
import com.app.gentlemanspa.ui.customerDashboard.fragment.serviceDetail.ServiceDetailFragmentArgs
import com.app.gentlemanspa.utils.CommonFunctions.decimalRoundToInt
import com.app.gentlemanspa.utils.ViewModelFactory
import com.app.gentlemanspa.utils.setGone
import com.app.gentlemanspa.utils.setVisible
import com.app.gentlemanspa.utils.showToast
import com.bumptech.glide.Glide


class ProductDetailFragment : Fragment(), View.OnClickListener {
    private var mainLoader: Int = 0
    private var count: Int = 0
    private var stock: Int = 0
    private lateinit var binding: FragmentProductDetailBinding
    private val args: ProductDetailFragmentArgs by navArgs()
    private val viewModel: ProductDetailViewModel by viewModels {
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
        // Inflate the layout for this fragment
        binding = FragmentProductDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        Log.d("productId", "productId->${args.productId}")
        viewModel.id.set(args.productId)
        viewModel.getProductDetails()
        binding.onClick = this
    }


    private fun callAddOrUpdateProductApi() {
        viewModel.productId.set(args.productId)
        viewModel.countInCart.set(count)
        viewModel.addProductInCart()
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.ivArrowBack -> {
                findNavController().popBackStack()
            }

            binding.ivMinus -> {
                count--
                if (count >= 1) {
                    binding.tvCount.text = count.toString()
                } else {
                    binding.clAddCart.setVisible()
                    binding.clPlusMinus.setGone()
                }
                callAddOrUpdateProductApi()
            }

            binding.clAddCart -> {
                count = 1
                binding.clAddCart.setGone()
                binding.clPlusMinus.setVisible()
                binding.tvCount.text = count.toString()
            }

            binding.ivPlus -> {
                if (count >= stock) {
                    requireContext().showToast("Can't add more than $stock items")
                } else {
                    count++
                    binding.tvCount.text = count.toString()
                    callAddOrUpdateProductApi()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initObserver() {
        viewModel.resultProductDetail.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        if (mainLoader == 0) {
                            mainLoader = 1
                            MyApplication.showProgress(requireContext())
                        }
                    }

                    Status.SUCCESS -> {
                        MyApplication.hideProgress()
                        val data = it.data?.data
                        binding.tvProductName.text = data?.name
                        // binding.tvTime.text = "${data?.durationInMinutes} mins"
                        binding.tvRupees.text = "$${decimalRoundToInt(data?.listingPrice)}"
                        binding.tvLessRupees.text = "$${decimalRoundToInt(data?.basePrice)}"
                        count = data?.countInCart!!
                        stock = data.stock!!
                        Log.d("productId", " inside api success countInCart->${data.countInCart}")
                        Log.d("productId", " inside api success count->${count}")

                        if (count >= 1) {
                            binding.clAddCart.setGone()
                            binding.clPlusMinus.setVisible()
                            binding.tvCount.text = count.toString()
                        } else {
                            binding.clAddCart.setVisible()
                            binding.clPlusMinus.setGone()
                        }
                        Glide.with(requireContext())
                            .load(ApiConstants.BASE_FILE + (data.images.get(0)))
                            .into(binding.ivProduct)


                        binding.tvDescription.text = data.description
                        binding.clFirst.setVisible()

                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        MyApplication.hideProgress()
                    }
                }
            }
        }

        viewModel.resultAddProductInCart.observe(this) {
            it.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        showProgress(requireContext())

                    }

                    Status.SUCCESS -> {
                        Log.d("resultAddProductInCart", "inside resultAddProductInCart SUCCESS")
                        Log.d(
                            "resultAddProductInCart",
                            "inside resultAddProductInCart Message->${it.message.toString()}"
                        )
                        hideProgress()

                    }

                    Status.ERROR -> {
                        Log.d("resultAddProductInCart", "inside resultAddProductInCart ERROR")
                        hideProgress()
                    }
                }
            }
        }
    }
}