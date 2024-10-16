package com.app.gentlemanspa.ui.customerDashboard.fragment.address

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
import com.app.gentlemanspa.databinding.FragmentAddressBinding
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.network.Status
import com.app.gentlemanspa.ui.customerDashboard.activity.CustomerActivity
import com.app.gentlemanspa.ui.customerDashboard.fragment.address.adapter.AddressAdapter
import com.app.gentlemanspa.ui.customerDashboard.fragment.address.model.AddressItem
import com.app.gentlemanspa.ui.customerDashboard.fragment.address.viewModel.AddressViewModel
import com.app.gentlemanspa.ui.customerDashboard.fragment.cart.CartFragmentDirections
import com.app.gentlemanspa.ui.customerDashboard.fragment.cart.viewModel.CartViewModel
import com.app.gentlemanspa.utils.ViewModelFactory
import com.app.gentlemanspa.utils.setGone
import com.app.gentlemanspa.utils.setVisible
import com.app.gentlemanspa.utils.showToast


class AddressFragment : Fragment(), View.OnClickListener {
    lateinit var binding: FragmentAddressBinding
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

    fun callGeCustomerAddressList() {
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
                        setDataOnAddressAdapter(it.data?.data!!)

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

    private fun setDataOnAddressAdapter(dataList: List<AddressItem>) {
        val addressAdapter = AddressAdapter(dataList)
        binding.rvAddresses.adapter = addressAdapter
        addressAdapter.setOnClickAddress(object : AddressAdapter.AddressCallbacks {
            override fun rootAddress() {

            }

            override fun addressOption() {
            }

            override fun deleteAddress(customerAddressId: Int) {
                Log.d("customerAddressId", "customerAddressId->${customerAddressId}")
                viewModel.customerAddressId.set(customerAddressId)
                viewModel.deleteCustomerAddress()
                // requireContext().showToast("show delete")
            }

        })

    }

    override fun onClick(v: View) {
        when (v) {
            binding.btnAddAddress -> {
                val action = AddressFragmentDirections.actionAddressFragmentToMapFragment()
                findNavController().navigate(action)

            }

            binding.ivArrowBack -> {
                findNavController().popBackStack()
            }
        }
    }

}


