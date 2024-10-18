package com.app.gentlemanspa.ui.customerDashboard.fragment.address

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.gentlemanspa.base.MyApplication.Companion.hideProgress
import com.app.gentlemanspa.base.MyApplication.Companion.showProgress
import com.app.gentlemanspa.databinding.FragmentAddressBinding
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.network.Status
import com.app.gentlemanspa.ui.customerDashboard.activity.CustomerActivity
import com.app.gentlemanspa.ui.customerDashboard.fragment.address.adapter.AddressAdapter
import com.app.gentlemanspa.ui.customerDashboard.fragment.address.model.AddressItem
import com.app.gentlemanspa.ui.customerDashboard.fragment.address.viewModel.AddressViewModel
import com.app.gentlemanspa.utils.ViewModelFactory
import com.app.gentlemanspa.utils.setGone
import com.app.gentlemanspa.utils.setVisible
import com.app.gentlemanspa.utils.showToast


class AddressFragment : Fragment(), View.OnClickListener {
    lateinit var binding: FragmentAddressBinding
    private var addressItemsList: ArrayList<AddressItem> = ArrayList()
    private val viewModel: AddressViewModel by viewModels {
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
        if (!this::binding.isInitialized) {
            binding = FragmentAddressBinding.inflate(layoutInflater, container, false)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as CustomerActivity).bottomNavigation(false)
        initUI()
    }

    private fun initUI() {
        callGeCustomerAddressList()
        binding.onClick = this
    }

    private fun callGeCustomerAddressList() {
        viewModel.geCustomerAddressList()

    }

    private fun initObserver() {
        viewModel.resultCustomerAddress.observe(this) {
            it.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        hideProgress()
                        addressItemsList.clear()

                        it.data?.data?.let { it1 -> addressItemsList.addAll(it1) }
                        setDataOnAddressAdapter()

                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        hideProgress()
                    }
                }
            }
        }
        viewModel.resultCustomerAddressStatus.observe(this) {
            it.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        hideProgress()
                     //   requireContext().showToast(it.message.toString())
                        val action=AddressFragmentDirections.actionAddressFragmentToCartFragment()
                        findNavController().navigate(action)

                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        hideProgress()
                    }
                }
            }
        }
        viewModel.resultCustomerDeleteAddress.observe(this) {
            it.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        hideProgress()
                        requireContext().showToast(it.message.toString())
                        callGeCustomerAddressList()

                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        hideProgress()
                    }
                }
            }
        }

    }

    private fun setDataOnAddressAdapter() {
        if (addressItemsList.size >= 3) {
            binding.btnAddAddress.setGone()
        } else {
            binding.btnAddAddress.setVisible()
        }
        val addressAdapter = AddressAdapter(addressItemsList)
        binding.rvAddresses.adapter = addressAdapter
        addressAdapter.setOnClickAddress(object : AddressAdapter.AddressCallbacks {
            override fun rootAddress(customerAddressId:Int) {
                Log.d("customerAddressId", "status customerAddressId->${customerAddressId}")
                viewModel.customerAddressId.set(customerAddressId)
                viewModel.primaryAddressStatus.set(true)
                viewModel.setCustomerAddressStatus()

            }

            override fun addressOption(addressItem: AddressItem) {
                val action = AddressFragmentDirections.actionAddressFragmentToMapFragment(
                    "Update Address",
                    addressItem.customerAddressId, addressItem.addressType
                )
                findNavController().navigate(action)
            }

            override fun deleteAddress(customerAddressId: Int) {
                Log.d("customerAddressId", "customerAddressId->${customerAddressId}")
                viewModel.customerAddressId.set(customerAddressId)
                viewModel.deleteCustomerAddress()
            }

        })

    }

    override fun onClick(v: View) {
        when (v) {
            binding.btnAddAddress -> {
                proceedToAddAddress()
            }

            binding.ivArrowBack -> {
                findNavController().popBackStack()
            }
        }
    }

    private fun proceedToAddAddress() {
        val addressMap = mutableMapOf<String, String?>()
        for (address in addressItemsList) {
            addressMap[address.addressType] = address.customerAddressId.toString()
        }
        val addressType = when {
            addressMap["Home"] == null -> "Home"
            addressMap["Work"] == null -> "Work"
            addressMap["Other"] == null -> "Other"
            else -> null
        }
        addressType?.let {
            val action = AddressFragmentDirections.actionAddressFragmentToMapFragment(
                "Add Address",
                0,
                it
            )
            findNavController().navigate(action)
        }

    }
}


