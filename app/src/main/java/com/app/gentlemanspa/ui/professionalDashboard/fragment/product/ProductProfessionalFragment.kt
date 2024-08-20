package com.app.gentlemanspa.ui.professionalDashboard.fragment.product

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.gentlemanspa.base.MyApplication
import com.app.gentlemanspa.databinding.FragmentProductProfessionalBinding
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.network.Status
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.model.ProductsListItem
import com.app.gentlemanspa.ui.professionalDashboard.activity.ProfessionalActivity
import com.app.gentlemanspa.ui.professionalDashboard.fragment.product.adapter.ProductProfessionalAdapter
import com.app.gentlemanspa.ui.professionalDashboard.fragment.product.viewModel.ProductProfessionalViewModel
import com.app.gentlemanspa.utils.ViewModelFactory
import com.app.gentlemanspa.utils.showToast


class ProductProfessionalFragment : Fragment(), View.OnClickListener {
    private lateinit var productProfessionalAdapter: ProductProfessionalAdapter
    private lateinit var binding: FragmentProductProfessionalBinding
    private var productsList: ArrayList<ProductsListItem> = ArrayList()
    private val viewModel: ProductProfessionalViewModel by viewModels {
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
        binding = FragmentProductProfessionalBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        (activity as ProfessionalActivity).bottomNavigation(false)
        viewModel.getProductsList()
        binding.onClick = this
    }

    private fun initObserver() {

        viewModel.resultProductsData.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {

                        MyApplication.showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        MyApplication.hideProgress()
                        productsList.clear()

                        it.data?.data?.dataList?.let { it1 -> productsList.addAll(it1) }
                        setProductsAdapter()

                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        MyApplication.hideProgress()
                    }
                }
            }
        }

        viewModel.resultDeleteProduct.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {

                        //MyApplication.showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        MyApplication.hideProgress()
                        setProductsAdapter()

                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        MyApplication.hideProgress()
                    }
                }
            }
        }

    }


    private fun setProductsAdapter() {
        productProfessionalAdapter = ProductProfessionalAdapter(productsList)
        binding.rvProduct.adapter = productProfessionalAdapter

        productProfessionalAdapter.setOnClickProductProfessional(object : ProductProfessionalAdapter.ProductProfessionalCallbacks {
            override fun rootProductProfessional(item: ProductsListItem) {

            }

            override fun deleteProductItem(item: ProductsListItem, position: Int) {
                deleteProductItemDialog(item ,position)
            }

            override fun updateProductItem(item: ProductsListItem) {
                val action = ProductProfessionalFragmentDirections.actionProductProfessionalFragmentToAddProductFragment(item,1)
                findNavController().navigate(action)
            }

        })
    }

    override fun onClick(v: View?) {
        when(v) {
            binding.clAddProduct -> {
                val action = ProductProfessionalFragmentDirections.actionProductProfessionalFragmentToAddProductFragment(null,0)
                findNavController().navigate(action)
            }

            binding.ivArrowBack -> {
                findNavController().popBackStack()
            }
        }

    }


    private fun deleteProductItemDialog(item: ProductsListItem, position: Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Alert")
        builder.setMessage("Are you sure you want to delete this product?")

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            viewModel.id.set(item.productId)
            viewModel.getDeleteProduct()
            productsList.removeAt(position)
        }

        builder.setNegativeButton(android.R.string.no) { dialog, which ->
            dialog.dismiss()
        }

        builder.show()
    }
}