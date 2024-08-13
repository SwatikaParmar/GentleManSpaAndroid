package com.app.gentlemanspa.ui.customerDashboard.fragment.product

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.app.gentlemanspa.base.MyApplication
import com.app.gentlemanspa.databinding.FragmentProductBinding
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.network.Status
import com.app.gentlemanspa.ui.customerDashboard.activity.CustomerActivity
import com.app.gentlemanspa.ui.customerDashboard.fragment.product.adapter.ProductsAdapter
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.model.ProductCategoriesItem
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.model.ProductsListItem
import com.app.gentlemanspa.ui.customerDashboard.fragment.product.adapter.CategoriesProductsAdapter
import com.app.gentlemanspa.ui.customerDashboard.fragment.product.viewModel.ProductViewModel
import com.app.gentlemanspa.utils.ViewModelFactory
import com.app.gentlemanspa.utils.showToast


class ProductFragment : Fragment() {
    private var productsList: ArrayList<ProductsListItem> = ArrayList()
    private lateinit var binding: FragmentProductBinding
    private var productCategoriesList: ArrayList<ProductCategoriesItem> = ArrayList()
    private var mainCategory : Int =0
    private var selectPosition : Int =0
    private val args : ProductFragmentArgs by navArgs()

    private val viewModel: ProductViewModel by viewModels {
        ViewModelFactory(
            InitialRepository()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainCategory = args.mainCategoryId
        selectPosition = args.selectPosition
        initObserver()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProductBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        (activity as CustomerActivity).bottomNavigation(false)
        viewModel.getProductCategories()
        viewModel.mainCategoryId.set(mainCategory)
        viewModel.getProductsList()
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

        viewModel.resultProductCategories.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                      //  MyApplication.showProgress(requireContext())


                    }

                    Status.SUCCESS -> {
                        MyApplication.hideProgress()
                        productCategoriesList.clear()

                        it.data?.data?.let { it1 -> productCategoriesList.addAll(it1) }
                        setCategoriesProductAdapter()

                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        MyApplication.hideProgress()
                    }
                }
            }
        }
    }

    private fun setCategoriesProductAdapter() {
        val categoriesProductsAdapter = CategoriesProductsAdapter(productCategoriesList,selectPosition)
        binding.rvProductCategories.adapter = categoriesProductsAdapter
        binding.rvProduct.scrollToPosition(selectPosition)

        categoriesProductsAdapter.setOnCategoriesProduct(object :CategoriesProductsAdapter.CategoriesProductCallbacks{
            override fun rootCategoriesProduct(item: ProductCategoriesItem, position: Int?) {
                val recyclerViewWidth = binding.rvProductCategories.width
                val layoutManager = binding.rvProductCategories.layoutManager
                val itemWidth = layoutManager?.getChildAt(0)?.width ?: 0
                val offset = (itemWidth * position!!) - (recyclerViewWidth / 2) + (itemWidth / 2)
                binding.rvProductCategories.smoothScrollBy(offset, 0)
                mainCategory = item.mainCategoryId
                viewModel.mainCategoryId.set(mainCategory)
                viewModel.getProductsList()
            }

        })
    }


    private fun setProductsAdapter() {
        val productsAdapter = ProductsAdapter(productsList)
        binding.rvProduct.adapter = productsAdapter

        productsAdapter.setOnClickProducts(object : ProductsAdapter.ProductsCallbacks {
            override fun rootProducts(item: ProductsListItem) {
                val action =
                    ProductFragmentDirections.actionProductFragmentToProductDetailFragment(
                        item.productId
                    )
                findNavController().navigate(action)
            }

        })
    }


}