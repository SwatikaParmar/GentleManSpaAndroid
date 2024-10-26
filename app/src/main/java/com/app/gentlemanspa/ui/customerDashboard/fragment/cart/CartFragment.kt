package com.app.gentlemanspa.ui.customerDashboard.fragment.cart

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.gentlemanspa.R
import com.app.gentlemanspa.base.MyApplication.Companion.hideProgress
import com.app.gentlemanspa.base.MyApplication.Companion.showProgress
import com.app.gentlemanspa.databinding.FragmentCartBinding
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.network.Status
import com.app.gentlemanspa.ui.customViews.AlertCallbackInt
import com.app.gentlemanspa.ui.customViews.AlertWithoutCancelCallbackInt
import com.app.gentlemanspa.ui.customViews.showAlertForPlaceOrder
import com.app.gentlemanspa.ui.customerDashboard.activity.CustomerActivity
import com.app.gentlemanspa.ui.customerDashboard.fragment.address.model.AddressItem
import com.app.gentlemanspa.ui.customerDashboard.fragment.cart.adapter.CartProductsAdapter
import com.app.gentlemanspa.ui.customerDashboard.fragment.cart.adapter.CartServicesAdapter
import com.app.gentlemanspa.ui.customerDashboard.fragment.cart.viewModel.CartViewModel
import com.app.gentlemanspa.ui.customerDashboard.fragment.selectProfessionalService.viewModel.SelectProfessionalServiceViewModel
import com.app.gentlemanspa.ui.customerDashboard.fragment.service.model.Product
import com.app.gentlemanspa.ui.customerDashboard.fragment.service.model.Service
import com.app.gentlemanspa.utils.ViewModelFactory
import com.app.gentlemanspa.utils.setGone
import com.app.gentlemanspa.utils.setVisible
import com.app.gentlemanspa.utils.showToast

class CartFragment : Fragment(), View.OnClickListener {
    lateinit var binding: FragmentCartBinding
    private val viewModel: CartViewModel by viewModels {
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
            binding = FragmentCartBinding.inflate(layoutInflater, container, false)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as CustomerActivity).bottomNavigation(false)
        initUI()
    }

    private fun initUI() {
        callGetCartItemsApi()
        viewModel.geCustomerAddressList()
        binding.onClick = this
        binding.cbAtVenue.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                Log.d("checkBox", "inside isChecked")
                binding.cbHomeDelivery.isChecked = false
                binding.clAddressViews.setGone()
            } else {
                Log.d("checkBox", "inside else of isChecked")
                binding.clAddressViews.setVisible()
                binding.cbHomeDelivery.isChecked = true
            }
        }

        binding.cbHomeDelivery.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                Log.d("checkBox", "inside cbHomeDelivery  isChecked")
                binding.cbAtVenue.isChecked = false
                binding.clAddressViews.setVisible()
            } else {
                Log.d("checkBox", "inside cbHomeDelivery else of isChecked")
                binding.clAddressViews.setGone()
                binding.cbAtVenue.isChecked = true

            }
        }
    }

    private fun callGetCartItemsApi() {
        viewModel.getCartItem()
    }

    @SuppressLint("SetTextI18n")
    private fun initObserver() {
        viewModel.resultGetCartItems.observe(this) {
            it.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        showProgress(requireContext())
                        Log.d("testIssue", "inside resultGetCartItems LOADING")

                    }

                    Status.SUCCESS -> {
                        Log.d("testIssue", "inside resultGetCartItems SUCCESS")
                        hideProgress()
                        if (it.data?.data?.cartServices!!.services.isNotEmpty() || it.data.data.cartProducts.products.isNotEmpty()){
                            binding.clNoDataFound.setGone()
                            binding.clPay.setVisible()
                            binding.clData.setVisible()
                            if (it.data.data.cartServices.services.isNotEmpty()) {
                                binding.clServices.setVisible()
                                binding.cvServicesTotal.setVisible()
                              //  binding.clPay.setVisible()
                                setCartServicesAdapter(it.data.data.cartServices.services)
                                binding.tvTotalPrice.text = "$${it.data.data.cartServices.totalMrp}"
                                binding.tvTotalDiscountPrice.text =
                                    "-$${it.data.data.cartServices.totalDiscount}"
                                binding.tvGrandTotalPrice.text =
                                    "$${it.data.data.cartServices.totalSellingPrice}"
                                binding.btnPay.text = "Pay $${it.data.data.spaTotalSellingPrice}.00"

                            } else {
                                binding.cvServicesTotal.setGone()
                                binding.clServices.setGone()
                             //   binding.clPay.setGone()

                            }

                            if (it.data.data.cartProducts.products.isNotEmpty()) {
                                binding.clProducts.setVisible()
                                binding.cvProductsTotal.setVisible()
                                binding.clDelivery.setVisible()
                                setCartProductsAdapter(it.data.data.cartProducts.products)
                                binding.tvProductTotalPrice.text =
                                    "$${it.data.data.cartProducts.totalMrp}"
                                binding.tvProductTotalDiscountPrice.text =
                                    "-$${it.data.data.cartProducts.totalDiscount}"
                                binding.tvProductGrandTotalPrice.text =
                                    "$${it.data.data.cartProducts.totalSellingPrice}"
                                binding.cbHomeDelivery.isChecked = true
                            } else {
                                binding.clProducts.setGone()
                                binding.clDelivery.setGone()
                                binding.cvProductsTotal.setGone()
                            }
                        }else{
                            binding.clPay.setGone()
                            binding.clData.setGone()
                            binding.clNoDataFound.setVisible()
                        }

                    }

                    Status.ERROR -> {
                        Log.d("testIssue", "inside resultGetCartItems ERROR")
                        requireContext().showToast(it.message.toString())
                        hideProgress()
                    }
                }
            }
        }

        viewModel.resultServiceToCart.observe(this) {
            it.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                    }
                    Status.SUCCESS -> {
                        hideProgress()
                        Log.d("testIssue", "inside resultServiceToCart SUCCESS")
                        requireContext().showToast(it.message.toString())
                        callGetCartItemsApi()
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
                        showProgress(requireContext())

                    }

                    Status.SUCCESS -> {
                        Log.d("testIssue", "inside resultAddProductInCart SUCCESS")
                        Log.d("testIssue", "inside resultAddProductInCart Message->${it.message.toString()}")
                        hideProgress()
                        //    requireContext().showToast(it.message.toString())
                        callGetCartItemsApi()
                    }

                    Status.ERROR -> {
                        Log.d("testIssue", "inside resultAddProductInCart ERROR")
                        hideProgress()
                        callGetCartItemsApi()
                    }
                }
            }


        }

        viewModel.resultCustomerAddress.observe(this) {
            it.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        //   showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        hideProgress()
                        val addressList = ArrayList<AddressItem>()
                        var primaryAddress = ""
                        addressList.clear()
                        it.data?.data?.let { it1 -> addressList.addAll(it1) }
                        for (i in addressList.indices) {
                            if (addressList[i].status) {
                                primaryAddress =
                                    "${addressList[i].houseNoOrBuildingName},${addressList[i].streetAddresss},${addressList[i].nearbyLandMark},${addressList[i].city},${addressList[i].state}"
                            }
                        }
                        if (primaryAddress.isNotEmpty()) {
                            binding.tvDeliveryAddress.setVisible()
                            binding.tvDeliveryAddress.text = primaryAddress
                        } else {
                            requireContext().showToast("Address is empty")
                            binding.tvDeliveryAddress.setGone()
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
                        Log.d("resultCustomerPlaceOrder", "inside resultCustomerPlaceOrder SUCCESS")
                        Log.d(
                            "resultCustomerPlaceOrder",
                            "inside resultCustomerPlaceOrder Message->${it.message.toString()}"
                        )
                        hideProgress()
                        requireContext().showAlertForPlaceOrder(it.message.toString(),object :
                            AlertWithoutCancelCallbackInt {
                            override fun onOkayClicked(view: View) {
                                val action=CartFragmentDirections.actionCartFragmentToHomeFragment()
                                 findNavController().navigate(action)
                            }



                        })
                        requireContext().showToast(it.message.toString())


                    }

                    Status.ERROR -> {
                        Log.d("resultCustomerPlaceOrder", "inside resultAddProductInCart ERROR")
                        hideProgress()
                    }
                }
            }


        }


    }

    private fun setCartServicesAdapter(servicesList: List<Service>) {
        val cartServicesAdapter = CartServicesAdapter(servicesList)
        binding.rvCartServices.adapter = cartServicesAdapter
        cartServicesAdapter.setOnCartServicesCallbacks(object :
            CartServicesAdapter.CartServicesCallbacks {
            override fun rootService(item: Service) {
            }
            override fun addOrRemoveService(item: Service) {
                viewModel.spaServiceId.set(item.spaServiceId)
                viewModel.slotId.set(0)
                viewModel.spaDetailId.set(item.spaDetailId)
                viewModel.serviceCountInCart.set(0)
                viewModel.addServiceToCart()
            }
            override fun addSlot(item: Service) {
                val action = CartFragmentDirections.actionCartFragmentAnyProfessionalFragment("Update Cart Service",
                    item.spaDetailId,
                    item.spaServiceId
                )
                findNavController().navigate(action)
            }

        })
    }

    private fun setCartProductsAdapter(productsList: List<Product>) {
        val cartProductAdapter = CartProductsAdapter(productsList)
        binding.rvCartProducts.adapter = cartProductAdapter
        cartProductAdapter.setOnClickCartProducts(object :
            CartProductsAdapter.CartProductsCallbacks {
            override fun rootProducts(item: Product) {

            }

            override fun addOrUpdateProductInCart(productId: Int, countInCard: Int, stock: Int) {
                Log.d(
                    "productId",
                    "productId->${productId}  countInCart->$countInCard  stock->$stock"
                )
                if (countInCard > stock) {
                    requireContext().showToast("Can't add more than $stock items")
                } else {
                    viewModel.productId.set(productId)
                    viewModel.countInCart.set(countInCard)
                    viewModel.addProductInCart()
                }
            }

        })

    }

    override fun onClick(v: View?) {
        when (v) {
            binding.ivAddressArrow -> {
                val action = CartFragmentDirections.actionCartFragmentAddressFragment()
                findNavController().navigate(action)
            }

            binding.ivArrowBack -> {
                findNavController().popBackStack()
            }
            binding.btnPay->{
                proceedToPlaceOrder()
            }
        }
    }

    private fun proceedToPlaceOrder() {
        viewModel.customerAddressId.set(0)
        viewModel.deliveryType.set("AtVenue")
        viewModel.paymentType.set("Cash")
        viewModel.customerPlaceOrder()
    }

}