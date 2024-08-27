package com.app.gentlemanspa.ui.professionalDashboard.fragment.productDetail

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.app.gentlemanspa.R
import com.app.gentlemanspa.base.MyApplication
import com.app.gentlemanspa.databinding.FragmentProductDetailBinding
import com.app.gentlemanspa.databinding.FragmentProductDetailProfessionalBinding
import com.app.gentlemanspa.network.ApiConstants
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.network.Status
import com.app.gentlemanspa.ui.customerDashboard.fragment.productDetail.ProductDetailFragmentArgs
import com.app.gentlemanspa.ui.customerDashboard.fragment.productDetail.viewModel.ProductDetailViewModel
import com.app.gentlemanspa.ui.professionalDashboard.fragment.productDetail.viewModel.ProductDetailProfessionalViewModel
import com.app.gentlemanspa.utils.CommonFunctions.decimalRoundToInt
import com.app.gentlemanspa.utils.ViewModelFactory
import com.app.gentlemanspa.utils.setGone
import com.app.gentlemanspa.utils.setVisible
import com.app.gentlemanspa.utils.showToast
import com.bumptech.glide.Glide


class ProductDetailProfessionalFragment : Fragment(), View.OnClickListener {
    private var mainLoader: Int = 0
    private lateinit var binding: FragmentProductDetailProfessionalBinding
    private val args: ProductDetailProfessionalFragmentArgs by navArgs()
    private val viewModel: ProductDetailProfessionalViewModel by viewModels {
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

                        Glide.with(requireContext())
                            .load(ApiConstants.BASE_FILE + (data?.images?.get(0)))
                            .into(binding.ivProduct)



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

}