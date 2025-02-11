package com.app.gentlemanspa.ui.customerDashboard.fragment.cart

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.app.gentlemanspa.R
import com.app.gentlemanspa.base.MyApplication.Companion.hideProgress
import com.app.gentlemanspa.base.MyApplication.Companion.showProgress
import com.app.gentlemanspa.databinding.FragmentCartBinding
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.network.Status
import com.app.gentlemanspa.utils.AlertWithoutCancelCallbackInt
import com.app.gentlemanspa.utils.showAlertForPlaceOrder
import com.app.gentlemanspa.ui.customerDashboard.activity.CustomerActivity
import com.app.gentlemanspa.ui.customerDashboard.fragment.address.model.AddressItem
import com.app.gentlemanspa.ui.customerDashboard.fragment.cart.adapter.CartProductsAdapter
import com.app.gentlemanspa.ui.customerDashboard.fragment.cart.adapter.CartServicesAdapter
import com.app.gentlemanspa.ui.customerDashboard.fragment.cart.model.PayByStripeRequest
import com.app.gentlemanspa.ui.customerDashboard.fragment.cart.viewModel.CartViewModel
import com.app.gentlemanspa.ui.customerDashboard.fragment.service.model.Product
import com.app.gentlemanspa.ui.customerDashboard.fragment.service.model.Service
import com.app.gentlemanspa.utils.AlertPaymentCallbackInt
import com.app.gentlemanspa.utils.AppPrefs
import com.app.gentlemanspa.utils.CUSTOMER_USER_ID
import com.app.gentlemanspa.utils.DELIVERY_ADDRESS
import com.app.gentlemanspa.utils.PROFESSIONAL_DETAIL_ID
import com.app.gentlemanspa.utils.ViewModelFactory
import com.app.gentlemanspa.utils.setGone
import com.app.gentlemanspa.utils.setVisible
import com.app.gentlemanspa.utils.showAlertForPayment
import com.app.gentlemanspa.utils.showToast
import com.google.firebase.firestore.auth.User

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
        binding.onClick = this
        callGetCartItemsApi()
        viewModel.geCustomerAddressList()
        setAddress()
    }

    private fun setAddress() {
        deliverAddress()
        binding.cbHomeDelivery.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                AppPrefs(requireContext()).saveStringPref(DELIVERY_ADDRESS, "Home Delivery")
                deliverAddress()
            } else {
                if (!binding.cbAtVenue.isChecked) {
                    binding.cbHomeDelivery.isChecked = true
                } else {
                    binding.cbHomeDelivery.isChecked = false

                }
            }

        }
        binding.cbAtVenue.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                AppPrefs(requireContext()).saveStringPref(DELIVERY_ADDRESS, "At Venue")
                deliverAddress()
            } else {
                if (!binding.cbHomeDelivery.isChecked) {
                    binding.cbAtVenue.isChecked = true
                } else {
                    binding.cbAtVenue.isChecked = false

                }
            }
        }
    }
    private fun deliverAddress() {
        if (AppPrefs(requireContext()).getStringPref(DELIVERY_ADDRESS).toString().isNotEmpty()) {
            when (AppPrefs(requireContext()).getStringPref(DELIVERY_ADDRESS)) {
                "At Venue" -> {
                    Log.d("deliveryAddress", "At Venue")
                    binding.cbAtVenue.isChecked = true
                    binding.cbHomeDelivery.isChecked = false
                    binding.clAddressViews.setGone()
                }

                "Home Delivery" -> {
                    Log.d("deliveryAddress", "Home Delivery")
                    binding.cbHomeDelivery.isChecked = true
                    binding.cbAtVenue.isChecked = false
                    binding.clAddressViews.setVisible()
                }

                else -> {
                    Log.d("deliveryAddress", "else")
                }
            }
        } else {
            Log.d("deliveryAddress", "deliveryAddress is empty")
            binding.cbHomeDelivery.isChecked = true
            binding.cbAtVenue.isChecked = false
            binding.clAddressViews.setVisible()

        }
    }
    private fun callGetCartItemsApi() {
        viewModel.getCartItem()
    }
    @SuppressLint("SetTextI18n", "DefaultLocale")
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
                        if (it.data?.data?.cartServices!!.services.isNotEmpty() || it.data.data.cartProducts.products.isNotEmpty()) {
                            binding.clNoDataFound.setGone()
                            binding.clPay.setVisible()
                            binding.clData.setVisible()
                            if (it.data.data.cartServices.services.isNotEmpty()) {
                                binding.clServices.setVisible()
                                binding.cvServicesTotal.setVisible()
                                //  binding.clPay.setVisible()
                                setCartServicesAdapter(it.data.data.cartServices.services)
                                binding.tvTotalPrice.text = "$${
                                    String.format(
                                        "%.2f",
                                        it.data.data.cartServices.totalMrp.toDouble()
                                    )
                                }"
                                binding.tvTotalDiscountPrice.text =
                                    "-$${
                                        String.format(
                                            "%.2f",
                                            it.data.data.cartServices.totalDiscount.toDouble()
                                        )
                                    }"
                                binding.tvGrandTotalPrice.text = "$${
                                    String.format(
                                        "%.2f",
                                        it.data.data.cartServices.totalSellingPrice.toDouble()
                                    )
                                }"
                                // binding.btnPay.text = "Pay $${it.data.data.spaTotalSellingPrice}.00"
                                binding.btnPay.text = "Pay $${
                                    String.format(
                                        "%.2f",
                                        it.data.data.spaTotalSellingPrice.toDouble()
                                    )
                                }"
                                Log.d(
                                    "testIssue",
                                    "spaTotalSellingPrice: ${{ it.data.data.spaTotalSellingPrice }}"
                                )


                            } else {
                                binding.cvServicesTotal.setGone()
                                binding.clServices.setGone()
                                //   binding.clPay.setGone()

                            }

                            if (it.data.data.cartProducts.products.isNotEmpty()) {
                                binding.clProducts.setVisible()
                                binding.cvProductsTotal.setVisible()
                                //     binding.clDelivery.setVisible()
                                setCartProductsAdapter(it.data.data.cartProducts.products)
                                binding.tvProductTotalPrice.text =
                                    "$${
                                        String.format(
                                            "%.2f",
                                            it.data.data.cartProducts.totalMrp.toDouble()
                                        )
                                    }"
                                binding.tvProductTotalDiscountPrice.text =
                                    "-$${
                                        String.format(
                                            "%.2f",
                                            it.data.data.cartProducts.totalDiscount.toDouble()
                                        )
                                    }"
                                binding.tvProductGrandTotalPrice.text =
                                    "$${
                                        String.format(
                                            "%.2f",
                                            it.data.data.cartProducts.totalSellingPrice.toDouble()
                                        )
                                    }"
                                //  binding.cbHomeDelivery.isChecked = true
                                binding.btnPay.text = "Pay $${
                                    String.format(
                                        "%.2f",
                                        it.data.data.spaTotalSellingPrice.toDouble()
                                    )
                                }"
                                deliverAddress()
                            } else {
                                binding.clProducts.setGone()
                                // binding.clDelivery.setGone()
                                binding.cvProductsTotal.setGone()
                                deliverAddress()
                            }
                        } else {
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
                        Log.d(
                            "testIssue",
                            "inside resultAddProductInCart Message->${it.message.toString()}"
                        )
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
                        var deliveryAddressName = ""
                        addressList.clear()
                        it.data?.data?.let { it1 -> addressList.addAll(it1) }
                        for (i in addressList.indices) {
                            if (addressList[i].status) {
                                primaryAddress =
                                    "${addressList[i].houseNoOrBuildingName},${addressList[i].streetAddresss},${addressList[i].nearbyLandMark},${addressList[i].city},${addressList[i].state}"
                                deliveryAddressName = addressList[i].addressType
                            }
                        }
                        if (primaryAddress.isNotEmpty()) {
                            binding.tvDeliveryAddress.setVisible()
                            var cleanPrimaryAddress = primaryAddress.replace(",,", ",")
                            if (cleanPrimaryAddress.endsWith(",")) {
                                cleanPrimaryAddress = cleanPrimaryAddress.dropLast(1)
                            }
                            binding.tvDeliveryAddress.text = cleanPrimaryAddress
                        } else {
                            //requireContext().showToast("Address is empty")
                            binding.tvDeliveryAddress.setGone()
                        }
                        if (deliveryAddressName.isNotEmpty()) {
                            binding.tvDeliveryPlaceName.text = "Delivering to $deliveryAddressName"

                        } else {
                            binding.tvDeliveryPlaceName.setGone()
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
                        requireContext().showAlertForPlaceOrder(it.message.toString(), object :
                            AlertWithoutCancelCallbackInt {
                            override fun onOkayClicked(view: View) {
                                val action =
                                    CartFragmentDirections.actionCartFragmentToHomeFragment()
                                findNavController().navigate(action)
                            }
                        })
                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        Log.d("resultCustomerPlaceOrder", "inside resultAddProductInCart ERROR")
                        hideProgress()
                    }
                }
            }
        }
        viewModel.resultPayByStripe.observe(this) {
            it.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        hideProgress()
                        Log.d(
                            "resultPayByStripe",
                            "sessionUrl->${it.data!!.data.sessionUrl} paymentId->${it.data.data.paymentId}"
                        )
                        if (it.data.data.sessionUrl.isNotBlank()) {
                            val action = CartFragmentDirections.actionCartFragmentToPaymentFragment(
                                it.data.data.sessionUrl,
                                it.data.data.paymentId
                            )
                            findNavController().navigate(action)
                        }
                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
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

            override fun addSlot(item: Service, status: String) {
                if (status == "update") {
                    val action = CartFragmentDirections.actionCartFragmentAnyProfessionalFragment(
                        "Reschedule CartFragment",
                        item.spaDetailId,
                        item.spaServiceId
                    )
                    findNavController().navigate(action)
                } else {
                    val action = CartFragmentDirections.actionCartFragmentAnyProfessionalFragment(
                        "Book CartFragment",
                        item.spaDetailId,
                        item.spaServiceId
                    )
                    findNavController().navigate(action)
                }
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
                // findNavController().popBackStack()
                findNavController().navigate(
                    R.id.homeCustomerFragment, null, NavOptions.Builder()
                        .setPopUpTo(
                            R.id.customer,
                            true
                        )  // Pop the entire back stack up to the navigation graph
                        .build()
                )
            }

            binding.btnPay -> {
                if (binding.clProducts.isVisible){
                    if (binding.cbHomeDelivery.isChecked && binding.tvDeliveryAddress.isGone){
                        requireContext().showToast("Please add your address")
                    }else{
                        proceedToPlaceOrder()
                    }
                }else{
                    proceedToPlaceOrder()
                }

            }
        }
    }
    private fun callCustomerPlaceOderApi() {
        viewModel.customerAddressId.set(0)
        viewModel.deliveryType.set("AtVenue")
        viewModel.paymentType.set("Cash")
        viewModel.paymentId.set(0)
        viewModel.customerPlaceOrder()
    }
    private fun proceedToPlaceOrder() {
        requireContext().showAlertForPayment(object : AlertPaymentCallbackInt {
            override fun onOkayClicked(view: View, onlinePayment: Boolean) {
                Log.d("paymentType", "onlinePayment->$onlinePayment")
                if (onlinePayment) {
                    val request = PayByStripeRequest(
                        AppPrefs(requireContext()).getStringPref(CUSTOMER_USER_ID).toString()
                    )
                    viewModel.payByStripeApi(request)
                } else {
                     callCustomerPlaceOderApi()
                }
            }
        })
    }

}