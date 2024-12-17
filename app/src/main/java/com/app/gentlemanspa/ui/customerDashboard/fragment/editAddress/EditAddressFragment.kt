package com.app.gentlemanspa.ui.customerDashboard.fragment.editAddress

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.location.Address
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.app.gentlemanspa.R
import com.app.gentlemanspa.base.MyApplication.Companion.hideProgress
import com.app.gentlemanspa.base.MyApplication.Companion.showProgress
import com.app.gentlemanspa.databinding.FragmentEditAddressBinding
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.network.Status
import com.app.gentlemanspa.ui.customerDashboard.fragment.address.model.AddressItem
import com.app.gentlemanspa.ui.customerDashboard.fragment.editAddress.viewModel.EditAddressViewModel
import com.app.gentlemanspa.utils.ViewModelFactory
import com.app.gentlemanspa.utils.showToast


class EditAddressFragment : Fragment(), View.OnClickListener {
   private lateinit var binding: FragmentEditAddressBinding

    private var homeId: Int = 0
    private var workId: Int = 0
    private var otherId: Int = 0
    private var addressType: String = ""
    private var addressHome: String = ""
    private var addressWork: String = ""
    private var addressOther: String = ""
    private var addressString: String = ""
    private var customerAddressId: Int = 0
    private var customerAddress: Address? = null
    private var street: String? = ""
    private var houseNo: String? = ""
    private var receiverPhoneNo: String? = ""
    private var altPhoneNo: String? = ""
    private var receiverName: String? = ""
    private var addressId: Int? = 0

    private val args: EditAddressFragmentArgs by navArgs()
    private var addressItemsList: ArrayList<AddressItem> = ArrayList()


    var addressStatusBoolean: Boolean? = false
    lateinit var EditAddressTypeItems: ArrayList<String>

    private val viewModel: EditAddressViewModel by viewModels {
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
        if (!this::binding.isInitialized){
            binding =FragmentEditAddressBinding.inflate(layoutInflater,container,false)

        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDataOnUI()
        initUI()
    }

    @SuppressLint("SetTextI18n")
    private fun setDataOnUI() {
        customerAddress = args.customerAddress
        addressType = args.addressType
        customerAddressId = args.addressId

        if (customerAddressId == 0) {
            binding.btnAddOrUpdateAddress!!.text = "Add"
            binding.tvHeader.text = "Add Address"
        } else {
            binding.btnAddOrUpdateAddress.text = "Update"
            binding.tvHeader.text = "Edit Address"
        }

        if (addressType == "Home") {
            selectAddressTypeColor()
            binding.llHomeBtn.setBackgroundResource(R.drawable.work_mode_selected_background)
        } else if (addressType == "Work") {
            selectAddressTypeColor()
            binding.llWorkBtn.setBackgroundResource(R.drawable.work_mode_selected_background)
        } else {
            selectAddressTypeColor()
            binding.llOtherBtn.setBackgroundResource(R.drawable.work_mode_selected_background)
        }

        if (customerAddressId == 0) {
            binding.tvHeader.text = "Add Address"
            binding.btnAddOrUpdateAddress.text = "Add"
        } else {
           // binding.tvHeader.text = "Edit Address"
            binding.tvHeader.text = "Update Address"
            binding.btnAddOrUpdateAddress.text = "Update"
        }
        setAddressDataLiveLocation(customerAddress)
    }

    @SuppressLint("SetTextI18n")
    private fun initUI() {
       viewModel.geCustomerAddressList()
        binding.onClick = this
    }

    @SuppressLint("SetTextI18n")
    private fun setAddressDataLiveLocation(addressArgs: Address?) {
        if (addressArgs != null) {
            if (addressArgs.getAddressLine(1) != null) {
                addressString = addressArgs.getAddressLine(1)
                binding.AddressText.text = addressArgs.getAddressLine(1)
            } else {
                addressString = addressArgs.getAddressLine(0)
                binding.AddressText.text = addressArgs.getAddressLine(0)
            }
            binding.etHouseNo.setText("")
            binding.etStreetAddress.setText(addressArgs.subLocality)
            binding.etPinCode.setText(addressArgs.postalCode)
            // binding.etNearbyLandmark.setText(addressArgs.locality)

            val arr = addressString.split(",").toTypedArray()
            if (arr.isNotEmpty()){
                if (addressArgs.subLocality == arr[1].trim()) {
                    binding.etHouseNo.setText(arr[0])
                } else {
                    binding.etHouseNo.setText("${arr[0]},${arr[1]}")
                }
            }else{
                requireContext().showToast("Something Went wrong")
            }



        }
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
                        for (i in addressItemsList.indices) {
                            if (addressItemsList[i].addressType == "Home") {
                                addressHome = "Home"
                                homeId = addressItemsList[i].customerAddressId
                            }
                            if (addressItemsList[i].addressType == "Work") {
                                addressWork = "Work"
                                workId = addressItemsList[i].customerAddressId
                            }

                            if (addressItemsList[i].addressType == "Other") {
                                addressOther = "Other"
                                otherId = addressItemsList[i].customerAddressId
                            }
                        }
                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        hideProgress()
                    }
                }
            }
        }
        viewModel.resultAddCustomerAddress.observe(this) {
            it.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        hideProgress()
                        requireContext().showToast(it.message.toString())
                        val action=EditAddressFragmentDirections.actionEditAddressFragmentToAddressFragment()
                        //Finish Current Fragment and previous fragment
                        val navOptions = NavOptions.Builder().setPopUpTo(R.id.editAddressFragment, true)
                            .setPopUpTo(R.id.addressFragment, true).build()
                        findNavController().navigate(action,navOptions)


                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        hideProgress()
                    }
                }
            }
        }
        viewModel.resultUpdateCustomerAddress.observe(this) {
            it.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        hideProgress()
                        requireContext().showToast(it.message.toString())
                        val action=EditAddressFragmentDirections.actionEditAddressFragmentToAddressFragment()
                        //Finish Current Fragment and previous fragment
                        val navOptions = NavOptions.Builder().setPopUpTo(R.id.editAddressFragment, true)
                            .setPopUpTo(R.id.addressFragment, true).build()
                        findNavController().navigate(action,navOptions)


                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        hideProgress()
                    }
                }
            }
        }
    }



    private fun selectAddressTypeColor() {
        binding.llHomeBtn.setBackgroundResource(R.drawable.work_mode_unselected_background)
        binding.llWorkBtn.setBackgroundResource(R.drawable.work_mode_unselected_background)
        binding.llOtherBtn.setBackgroundResource(R.drawable.work_mode_unselected_background)
    }

    override fun onClick(v: View) {
        when (v) {
            binding.ivArrowBack-> {
                findNavController().popBackStack()
            }
            binding.llHomeBtn->{
                performActionOnHome()
            }
            binding.llWorkBtn-> {
                performActionOnWork()

            }
            binding.llOtherBtn -> {
                performActionOnOther()
            }
            binding.btnAddOrUpdateAddress-> {
                proceedToAddOrUpdateAddress()

            }

        }
    }

    @SuppressLint("SetTextI18n")
    private fun performActionOnOther() {
        selectAddressTypeColor()
        binding.llOtherBtn.setBackgroundResource(R.drawable.work_mode_selected_background)
        addressType = "Other"
        if (otherId == 0) {
            binding.tvHeader.text = "Add Address"
            binding.btnAddOrUpdateAddress.text = "Add"
        } else {
            binding.tvHeader.text = "Edit Address"
            binding.btnAddOrUpdateAddress.text = "Update"
            customerAddressId = otherId
        }
    }

    @SuppressLint("SetTextI18n")
    private fun performActionOnWork() {
        selectAddressTypeColor()
        binding.llWorkBtn.setBackgroundResource(R.drawable.work_mode_selected_background)
        addressType = "Work"
        if (workId == 0) {
            binding.tvHeader.text = "Add Address"
            binding.btnAddOrUpdateAddress.text = "Add"
        } else {
            binding.tvHeader.text = "Edit Address"
            binding.btnAddOrUpdateAddress.text = "Update"
            customerAddressId = workId
        }
    }

    @SuppressLint("SetTextI18n")
    private fun performActionOnHome() {
        selectAddressTypeColor()
        binding.llHomeBtn.setBackgroundResource(R.drawable.work_mode_selected_background)
        addressType = "Home"
        if (homeId == 0) {
            binding.tvHeader.text = "Add Address"
            binding.btnAddOrUpdateAddress.text = "Add"
        } else {
            binding.tvHeader.text = "Edit Address"
            binding.btnAddOrUpdateAddress.text = "Update"
            customerAddressId = homeId
        }
    }

    private fun proceedToAddOrUpdateAddress() {
            street = binding.etStreetAddress.text.toString()
            houseNo = binding.etHouseNo.text.toString()
            receiverPhoneNo = binding.etReceiverPhone.text.toString()
            receiverName = binding.etReceiverName.text.toString()
            altPhoneNo = binding.etAltPhoneNo.text.toString()
            when {
                receiverName?.trim() == "" -> {
                    binding.etReceiverName.requestFocus()
                    binding.etReceiverName.error = "Please enter receiver's name"
                }

                receiverPhoneNo?.trim() == "" -> {
                    binding.etReceiverPhone.requestFocus()
                    binding.etReceiverPhone.error = "Please enter receiver's phone no."
                }

                receiverPhoneNo?.trim()?.length!! < 10 -> {
                    binding.etReceiverPhone.requestFocus()
                    binding.etReceiverPhone.error = "Please enter valid phone no."
                }

                houseNo?.trim() == "" -> {
                    binding.etHouseNo.requestFocus()
                    binding.etHouseNo.error = "Please enter house no."
                }

                street?.trim() == "" -> {
                    binding.etStreetAddress.requestFocus()
                    binding.etStreetAddress.error = "Please enter street."
                }

                else -> {
                    if (altPhoneNo?.trim()?.length!! > 0) {
                        if (altPhoneNo?.trim()?.length!! < 10) {
                            binding.etAltPhoneNo.requestFocus()
                            binding.etAltPhoneNo.error = "Please enter valid alternate phone no."
                        } else {
                            switchCase()
                        }
                    } else {
                        switchCase()
                    }


                }
            }
        }


    private fun switchCase(){
        Log.d("addressDetail","adminArea->${customerAddress?.adminArea.toString()}")
        Log.d("addressDetail","locality->${customerAddress?.locality.toString()}")
        Log.d("addressDetail","street->${street}")
        if (binding.btnAddOrUpdateAddress.text.toString() == "Add"){
            viewModel.addCustomerAddress(0,
                binding.etReceiverName.text.toString(),
                binding.etReceiverPhone.text.toString(),
                binding.etPinCode.text.toString(),
                customerAddress?.adminArea.toString(),
                customerAddress?.locality.toString(),
                "",
                street!!,
                binding.etNearbyLandmark.text.toString(),
                addressType,
                customerAddress?.latitude.toString(),
                customerAddress?.longitude.toString(),
                binding.etAltPhoneNo.text.toString(),

                )
        }else{
          requireContext().showToast("Update")
            viewModel.updateCustomerAddress(customerAddressId,
                binding.etReceiverName.text.toString(),
                binding.etReceiverPhone.text.toString(),
                binding.etPinCode.text.toString(),
                customerAddress?.adminArea.toString(),
                customerAddress?.locality.toString(),
                "",
                street!!,
                binding.etNearbyLandmark.text.toString(),
                addressType,
                customerAddress?.latitude.toString(),
                customerAddress?.longitude.toString(),
                binding.etAltPhoneNo.text.toString(),

                )
        }


    }
}


