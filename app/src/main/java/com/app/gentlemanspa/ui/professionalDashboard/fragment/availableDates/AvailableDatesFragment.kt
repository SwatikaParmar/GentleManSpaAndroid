package com.app.gentlemanspa.ui.professionalDashboard.fragment.availableDates

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
import com.app.gentlemanspa.databinding.FragmentAvailableDatesBinding
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.network.Status
import com.app.gentlemanspa.ui.professionalDashboard.activity.ProfessionalActivity
import com.app.gentlemanspa.ui.professionalDashboard.fragment.availableDates.adapter.ExpertAvailableDatesAdapter
import com.app.gentlemanspa.ui.professionalDashboard.fragment.availableDates.adapter.ExpertBlockDatesAdapter
import com.app.gentlemanspa.ui.professionalDashboard.fragment.availableDates.model.SlotStatusRequest
import com.app.gentlemanspa.ui.professionalDashboard.fragment.availableDates.viewModel.AvailableDatesViewModel
import com.app.gentlemanspa.utils.AppPrefs
import com.app.gentlemanspa.utils.PROFESSIONAL_DETAIL_ID
import com.app.gentlemanspa.utils.ViewModelFactory
import com.app.gentlemanspa.utils.setGone
import com.app.gentlemanspa.utils.setVisible
import com.app.gentlemanspa.utils.showToast

class AvailableDatesFragment : Fragment(), View.OnClickListener {
    lateinit var binding: FragmentAvailableDatesBinding
    private val viewModel: AvailableDatesViewModel by viewModels {
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
        binding = FragmentAvailableDatesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        binding.onClick=this
        (activity as ProfessionalActivity).bottomNavigation(false)
        callExpertBlockDatesApi()
        callExpertAvailableDatesApi()
    }

    private fun callExpertBlockDatesApi() {
        viewModel.professionalId.set(AppPrefs(requireContext()).getStringPref(PROFESSIONAL_DETAIL_ID)?.toInt())
        viewModel.availabilityState.set("unavailable")
        viewModel.getProfessionalBlockDates()
    }

    private fun callExpertAvailableDatesApi() {
        viewModel.professionalId.set(AppPrefs(requireContext()).getStringPref(PROFESSIONAL_DETAIL_ID)?.toInt())
        viewModel.getProfessionalAvailableDates()
    }
    private fun setExpertBlockDatesAdapter(data: List<String>) {
        val expertBlockDatesAdapter = ExpertBlockDatesAdapter(data)
        binding.rvBlockDates.adapter = expertBlockDatesAdapter
        expertBlockDatesAdapter.setExpertBlockDatesCallBacks(object : ExpertBlockDatesAdapter.ExpertBlockDatesCallBacks{
            override fun expertUnBlockDate(date: String) {
                val professionalDetailId = AppPrefs(requireContext()).getStringPref(PROFESSIONAL_DETAIL_ID)?.toInt()
                val request= SlotStatusRequest(professionalDetailId!!,date,true)
                Log.d("BlockDate","unblock request->${request}")
                viewModel.slotStatusApi(request)
            }
        })

    }
    private fun setExpertAvailableDatesAdapter(data: List<String>) {
        val expertAvailableDatesAdapter = ExpertAvailableDatesAdapter(data)
        binding.rvAvailableDates.adapter = expertAvailableDatesAdapter
        expertAvailableDatesAdapter.setExpertAvailableDatesCallbacks(object:
            ExpertAvailableDatesAdapter.ExpertAvailableDatesCallbacks{
            override fun expertBlockDate(date: String) {
                val professionalDetailId =
                    AppPrefs(requireContext()).getStringPref(PROFESSIONAL_DETAIL_ID)?.toInt()
                val request= SlotStatusRequest(professionalDetailId!!,date,false)
                Log.d("BlockDate","block request->${request}")
                viewModel.slotStatusApi(request)
            }
        })
    }
    private fun initObserver() {
        viewModel.resultProfessionalBlockDates.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                     //   showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        hideProgress()
                        Log.d("BlockDate","ExpertBlockDates->${it.data?.data}")

                        if (it.data?.data != null && it.data.data.isNotEmpty()) {
                            binding.llBlockDates.setVisible()
                            setExpertBlockDatesAdapter(it.data.data)
                        }else{
                            binding.llBlockDates.setGone()
                        }
                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        hideProgress()
                    }
                }
            }
        }
        viewModel.resultExpertAvailableDates.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                       // showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        hideProgress()
                        Log.d("BlockDate","availableDates->${it.data?.data}")

                        if (it.data?.data != null && it.data.data.isNotEmpty()) {
                            binding.llAvailableDates.setVisible()
                            setExpertAvailableDatesAdapter(it.data.data)
                        }else{
                            binding.llAvailableDates.setGone()
                        }
                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        hideProgress()
                    }
                }
            }
        }
        viewModel.resultSlotStatus.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                         showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        hideProgress()
                        Log.d("BlockDate","slotStatus->${it.data}")
                        requireContext().showToast(it.data!!.messages)
                        callExpertBlockDatesApi()
                        callExpertAvailableDatesApi()
                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        hideProgress()
                    }
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v){
            binding.ivBack->{
                findNavController().popBackStack()
            }
        }
    }


}