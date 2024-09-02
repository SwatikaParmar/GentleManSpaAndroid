package com.app.gentlemanspa.ui.professionalDashboard.fragment.productDetail

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.app.gentlemanspa.base.MyApplication
import com.app.gentlemanspa.databinding.FragmentProductDetailProfessionalBinding
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.network.Status
import com.app.gentlemanspa.ui.professionalDashboard.fragment.productDetail.adapter.ProductDetailBannerAdapter
import com.app.gentlemanspa.ui.professionalDashboard.fragment.productDetail.viewModel.ProductDetailProfessionalViewModel
import com.app.gentlemanspa.utils.CommonFunctions.decimalRoundToInt
import com.app.gentlemanspa.utils.ViewModelFactory
import com.app.gentlemanspa.utils.setVisible
import com.app.gentlemanspa.utils.showToast
import com.google.android.material.tabs.TabLayoutMediator


class ProductDetailProfessionalFragment : Fragment(), View.OnClickListener {
    private var productBannerList: ArrayList<String> = ArrayList()
    private var mainLoader: Int = 0
    private lateinit var binding: FragmentProductDetailProfessionalBinding
    private val args: ProductDetailProfessionalFragmentArgs by navArgs()
    private val viewModel: ProductDetailProfessionalViewModel by viewModels {
        ViewModelFactory(
            InitialRepository()
        )
    }
    private val headerHandler: Handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initObserver()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProductDetailProfessionalBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        viewModel.id.set(args.productId)
        viewModel.getProductDetails()
        binding.onClick = this
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
                        productBannerList.clear()
                        data?.images?.let { it1 -> productBannerList.addAll(it1) }
                        setProductBannerAdapter()
                        /*Glide.with(requireContext())
                            .load(ApiConstants.BASE_FILE + (data?.images?.get(0)))
                            .into(binding.ivProduct)*/

                        binding.tvDescription.text = data?.description

                        binding.clFirst.setVisible()

                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        MyApplication.hideProgress()
                    }
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.ivArrowBack -> {
                findNavController().popBackStack()
            }
        }
    }


    private fun setProductBannerAdapter() {
       var productDetailBannerAdapter = ProductDetailBannerAdapter(productBannerList)
        binding.vpBanner.adapter = productDetailBannerAdapter

        if (productBannerList.size > 1) {
            binding.tlBanner.visibility = View.VISIBLE
        } else {
            binding.tlBanner.visibility = View.GONE
        }

        TabLayoutMediator(binding.tlBanner, binding.vpBanner) { tab, position ->
        }.attach()

        binding.vpBanner.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                headerHandler.removeCallbacks(headerRunnable)
                headerHandler.postDelayed(headerRunnable, 3000)

                // Slide duration 3 seconds
            }
        })
    }

    private val headerRunnable = Runnable {
        if (productBannerList.size > binding.vpBanner.currentItem + 1)
            binding.vpBanner.setCurrentItem(binding.vpBanner.currentItem + 1, true)
        else
            binding.vpBanner.setCurrentItem(0, true)
    }


    override fun onResume() {
        super.onResume()
        headerHandler.postDelayed(headerRunnable, 2000) // Slide duration 3 seconds
    }

    override fun onPause() {
        headerHandler.removeCallbacks(headerRunnable)
        super.onPause()

    }

}