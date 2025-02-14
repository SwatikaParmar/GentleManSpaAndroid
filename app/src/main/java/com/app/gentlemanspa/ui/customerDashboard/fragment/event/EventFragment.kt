package com.app.gentlemanspa.ui.customerDashboard.fragment.event

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
import com.app.gentlemanspa.databinding.FragmentEventBinding
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.network.Status
import com.app.gentlemanspa.ui.customerDashboard.activity.CustomerActivity
import com.app.gentlemanspa.ui.customerDashboard.fragment.event.adapter.EventAdapter
import com.app.gentlemanspa.ui.customerDashboard.fragment.event.model.AddOrUpdateEventRegistrationRequest
import com.app.gentlemanspa.ui.customerDashboard.fragment.event.model.EventListData
import com.app.gentlemanspa.ui.customerDashboard.fragment.event.viewModel.EventViewModel
import com.app.gentlemanspa.utils.AppPrefs
import com.app.gentlemanspa.utils.CUSTOMER_USER_ID
import com.app.gentlemanspa.utils.ViewModelFactory
import com.app.gentlemanspa.utils.openGMapAndRedirectToLocation
import com.app.gentlemanspa.utils.showMessageDialog
import com.app.gentlemanspa.utils.showToast

class EventFragment : Fragment(), View.OnClickListener {
    lateinit var binding: FragmentEventBinding
    private val viewModel: EventViewModel by viewModels {
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
    ):View{
        if (!this::binding.isInitialized){
            binding=FragmentEventBinding.inflate(layoutInflater,container,false)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        (activity as CustomerActivity).bottomNavigation(false)
        binding.onClick=this
        callEventListApi()
    }

    private fun callEventListApi(){
        viewModel.userId.set(AppPrefs(requireContext()).getStringPref(
            CUSTOMER_USER_ID).toString())
        viewModel.getEventListApi()
    }

    private fun setEventAdapter(eventListData:List<EventListData>) {
        val eventAdapter = EventAdapter(requireContext(),eventListData)
        binding.rvEvents.adapter=eventAdapter
        eventAdapter.setEventCallbacks(object :EventAdapter.EventCallbacks{
            override fun addOrUpdateEventRegistration(item: EventListData) {
                if (item.isRegistered){
                    showMessageDialog(requireContext(),"Cancel Event?",getString(R.string.are_you_sure_you_want_to_cancel_your_registration_for_this_event)){
                        val request=AddOrUpdateEventRegistrationRequest(item.eventId,AppPrefs(requireContext()).getStringPref(CUSTOMER_USER_ID).toString(),
                            false,"")
                        Log.d("AddOrUpdateEvent","request->$request")
                        viewModel.addOrUpdateEventRegistrationApi(request)
                    }
                }else{
                    val request=AddOrUpdateEventRegistrationRequest(item.eventId,AppPrefs(requireContext()).getStringPref(CUSTOMER_USER_ID).toString(),
                        true,"")
                    Log.d("AddOrUpdateEvent","request->$request")
                    viewModel.addOrUpdateEventRegistrationApi(request)
                }
            }

            override fun redirectToEventLocation(item: EventListData) {
                val latitude = 30.7055
                val longitude = 76.8000
                openGMapAndRedirectToLocation(requireContext(),latitude,longitude)
            }
        })
    }

    override fun onClick(v: View) {
        when(v){
            binding.ivArrowBack ->{
                findNavController().popBackStack()
            }
        }
    }
    private fun initObserver() {
        viewModel.resultEventList.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        hideProgress()
                        Log.d("Event","EventList->${it.data?.data}")
                        if (it.data?.data!!.isNotEmpty()){
                            setEventAdapter(it.data.data)
                        }else{
                            Log.d("Event","EventList is empty")
                        }
                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        hideProgress()
                    }
                }
            }
        }
        viewModel.resultAddOrUpdateEventRegistration.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        hideProgress()
                        Log.d("AddOrUpdateEvent","data->${it.data}")
                        if (it.data?.isSuccess == true){
                           callEventListApi()
                        }
                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        hideProgress()
                    }
                }
            }
        }

    }
}