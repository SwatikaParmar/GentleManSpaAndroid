package com.app.gentlemanspa.ui.customerDashboard.fragment.product

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.app.gentlemanspa.base.MyApplication.Companion.hideProgress
import com.app.gentlemanspa.base.MyApplication.Companion.showProgress
import com.app.gentlemanspa.databinding.FragmentProductBinding
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.network.Status
import com.app.gentlemanspa.ui.customerDashboard.activity.CustomerActivity
import com.app.gentlemanspa.ui.customerDashboard.fragment.product.adapter.ProductsAdapter
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.model.ProductCategoriesItem
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.model.ProductsListItem
import com.app.gentlemanspa.ui.customerDashboard.fragment.product.adapter.CategoriesProductsAdapter
import com.app.gentlemanspa.ui.customerDashboard.fragment.product.viewModel.ProductViewModel
import com.app.gentlemanspa.ui.customerDashboard.fragment.service.ServiceFragmentDirections
import com.app.gentlemanspa.utils.ViewModelFactory
import com.app.gentlemanspa.utils.formatPrice
import com.app.gentlemanspa.utils.setGone
import com.app.gentlemanspa.utils.setVisible
import com.app.gentlemanspa.utils.showToast


class ProductFragment : Fragment(), View.OnClickListener  {
    private var productsList: ArrayList<ProductsListItem> = ArrayList()
    private lateinit var binding: FragmentProductBinding
    private var productCategoriesList: ArrayList<ProductCategoriesItem> = ArrayList()
    private var mainCategory : Int =0
    private var selectPosition : Int =0
    private val spaDetailId = 21
    private val args : ProductFragmentArgs by navArgs()
    private val handler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable? = null

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
    ): View {
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
        callProductListApi("")
        callGetCartItemsApi()
        binding.onClick = this
        binding.etSearchService.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Remove any previously scheduled searches
                searchRunnable?.let { handler.removeCallbacks(it) }
                // Schedule a new search after 2 seconds
                searchRunnable = Runnable {
                    searchServiceByName(s.toString())
                }
                handler.postDelayed(searchRunnable!!, 2000)
            }
            override fun afterTextChanged(s: Editable?) {}
        })

    }
    fun searchServiceByName(searchQuery:String){
        callProductListApi(searchQuery)

    }

    private fun callProductListApi(searchQuery: String){
        viewModel.mainCategoryId.set(mainCategory)
        viewModel.spaDetailId.set(spaDetailId)
        viewModel.searchQuery.set(searchQuery)
        viewModel.getProductsList()
    }
    private fun callGetCartItemsApi() {
        viewModel.getProductCartItem()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null) // Remove all callbacks

    }
    @SuppressLint("SetTextI18n")
    private fun initObserver() {

        viewModel.resultProductsData.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                       showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        hideProgress()
                        Log.d("testIssue","inside resultProductsData Success")
                        productsList.clear()
                        it.data?.data?.dataList?.let { it1 -> productsList.addAll(it1) }
                        setProductsAdapter()

                    }

                    Status.ERROR -> {
                        Log.d("testIssue","inside resultProductsData Failure")
                        requireContext().showToast(it.message.toString())
                        hideProgress()
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
                        hideProgress()
                        productCategoriesList.clear()

                        it.data?.data?.let { it1 -> productCategoriesList.addAll(it1) }
                        setCategoriesProductAdapter()

                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        hideProgress()
                    }
                }
            }
        }

        viewModel.resultGetCartItems.observe(this) {
            it.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        /*  if (mainLoader == 0) {
                              mainLoader = 1
                              MyApplication.showProgress(requireContext())
                          }*/
                    }

                    Status.SUCCESS -> {
                        hideProgress()
               //         requireContext().showToast(it.message.toString())
                        Log.d("testIssue","inside resultAddProductInCart SUCCESS")

                        if (it.data?.data?.cartProducts?.totalItem!! > 0){
                            binding.clBooking.setVisible()
                            val priceFormat= formatPrice(it.data.data.cartProducts.totalSellingPrice.toDouble())
                            binding.tvRupees.text ="$$priceFormat"
                            if (it.data.data.cartProducts.totalItem > 1){
                                binding.tvSelectServices.text = "${it.data.data.cartProducts.totalItem} Products added"
                            }else{
                                binding.tvSelectServices.text = "${it.data.data.cartProducts.totalItem} Product added"
                            }
                        }else{
                            binding.clBooking.setGone()
                        }

                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        hideProgress()
                    }
                }
            }


        }
        viewModel.resultAddProductInCart.observe(this) {
            it.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        /*  if (mainLoader == 0) {
                              mainLoader = 1
                              MyApplication.showProgress(requireContext())
                          }*/
                        showProgress(requireContext())

                    }

                    Status.SUCCESS -> {
                        Log.d("testIssue","inside resultAddProductInCart SUCCESS")
                        Log.d("testIssue","inside resultAddProductInCart Message->${it.message.toString()}")
                        hideProgress()
                    //    requireContext().showToast(it.message.toString())
                        callProductListApi("")
                        callGetCartItemsApi()


                    }

                    Status.ERROR -> {
                        Log.d("testIssue","inside resultAddProductInCart ERROR")
                        hideProgress()
                //        requireContext().showToast(it.message.toString())
                        callProductListApi("")
                        callGetCartItemsApi()
                    }
                }
            }


        }
    }

    private fun setCategoriesProductAdapter() {
        Log.d("dataProducts","selectPosition->$selectPosition")
       // productCategoriesList.reverse()
        val categoriesProductsAdapter = CategoriesProductsAdapter(productCategoriesList,selectPosition)
        binding.rvProductCategories.adapter = categoriesProductsAdapter
       // binding.rvProduct.scrollToPosition(selectPosition)

        categoriesProductsAdapter.setOnCategoriesProduct(object :CategoriesProductsAdapter.CategoriesProductCallbacks{
            override fun rootCategoriesProduct(item: ProductCategoriesItem, position: Int?) {
                val recyclerViewWidth = binding.rvProductCategories.width
                val layoutManager = binding.rvProductCategories.layoutManager
                val itemWidth = layoutManager?.getChildAt(0)?.width ?: 0
                val offset = (itemWidth * position!!) - (recyclerViewWidth / 2) + (itemWidth / 2)
                binding.rvProductCategories.smoothScrollBy(offset, 0)
                mainCategory = item.mainCategoryId
             /*   viewModel.mainCategoryId.set(mainCategory)
                viewModel.getProductsList()*/
                callProductListApi("")
            }

        })

        if (selectPosition != -1){
            binding.rvProductCategories.scrollToPosition(selectPosition)
        }
    }


    private fun setProductsAdapter() {
        Log.d("testIssue","inside setProductsAdapter ")

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

            override fun addOrUpdateProductInCart(productId:Int, countInCard:Int,stock:Int) {
                Log.d("productId","productId->${productId}  countInCart->$countInCard  stock->$stock")
                if (countInCard>stock){
                  requireContext().showToast("Can't add more than $stock items")
                }else{
                    viewModel.productId.set(productId)
                    viewModel.countInCart.set(countInCard)
                    viewModel.addProductInCart()
                }
            }
        })



    }
    override fun onClick(v: View?) {
        when(v) {
            binding.btnContinue -> {
                //requireContext().showToast("Continue is clicked")
                val action = ProductFragmentDirections.actionProductFragmentToCartFragment()
                findNavController().navigate(action)
            }

            binding.ivArrowBack -> {
                findNavController().popBackStack()
            }
        }
    }

}