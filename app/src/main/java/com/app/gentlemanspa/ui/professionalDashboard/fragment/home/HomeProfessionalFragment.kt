package com.app.gentlemanspa.ui.professionalDashboard.fragment.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.app.gentlemanspa.R
import com.app.gentlemanspa.base.MyApplication
import com.app.gentlemanspa.databinding.FragmentHomeProfessionalBinding
import com.app.gentlemanspa.network.ApiConstants.BASE_FILE
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.network.Status
import com.app.gentlemanspa.ui.customerDashboard.activity.CustomerActivity
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.model.RegisterUserInFirebaseRequest
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.model.UserState
import com.app.gentlemanspa.ui.professionalDashboard.activity.ProfessionalActivity
import com.app.gentlemanspa.ui.professionalDashboard.fragment.home.adapter.ConfirmedAppointmentAdapter
import com.app.gentlemanspa.ui.professionalDashboard.fragment.home.adapter.PastAppointmentAdapter
import com.app.gentlemanspa.ui.professionalDashboard.fragment.home.adapter.UpcomingAppointmentAdapter
import com.app.gentlemanspa.ui.professionalDashboard.fragment.home.viewModel.HomeProfessionalViewModel
import com.app.gentlemanspa.ui.professionalDashboard.fragment.profile.viewModel.ProfileProfessionalDetailViewModel
import com.app.gentlemanspa.utils.AppPrefs
import com.app.gentlemanspa.utils.FCM_TOKEN
import com.app.gentlemanspa.utils.PROFESSIONAL_DETAIL_ID
import com.app.gentlemanspa.utils.PROFESSIONAL_USER_ID
import com.app.gentlemanspa.utils.USER_ID
import com.app.gentlemanspa.utils.ViewModelFactory
import com.app.gentlemanspa.utils.getCustomerCurrentDate
import com.app.gentlemanspa.utils.getCustomerCurrentTime
import com.app.gentlemanspa.utils.showToast
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class HomeProfessionalFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentHomeProfessionalBinding
    private lateinit var profileUpdatedListener: OnProfileUpdatedListener

    private val viewModel: HomeProfessionalViewModel by viewModels { ViewModelFactory(
        InitialRepository()
    ) }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnProfileUpdatedListener) {
            profileUpdatedListener = context
        } else {
            throw ClassCastException("$context must implement OnProfileUpdatedListener")
        }
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
        binding = FragmentHomeProfessionalBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        registerProfessionalInFirebase()

    }

    private fun registerProfessionalInFirebase() {
        Log.e("FirebaseRegister", "${AppPrefs(requireContext()).getStringPref(PROFESSIONAL_USER_ID)}")

        val profileProfessionalData= AppPrefs(requireContext()).getProfileProfessionalData("PROFILE_DATA")
        if (profileProfessionalData?.data?.firstName.toString().isNotEmpty() && profileProfessionalData?.data?.email.toString().isNotEmpty() && profileProfessionalData?.data?.gender.toString().isNotEmpty()) {
            val userState = UserState(getCustomerCurrentDate(), "online", getCustomerCurrentTime())
            val user = RegisterUserInFirebaseRequest(
                uid = "${AppPrefs(requireContext()).getStringPref(PROFESSIONAL_USER_ID)}",
                email = profileProfessionalData?.data?.email.toString(),
                fcm_token = "${AppPrefs(requireContext()).getStringPref(FCM_TOKEN)}",
                gender = profileProfessionalData?.data?.gender.toString(),
                image = "${profileProfessionalData?.data?.profilepic}",
                name = "${profileProfessionalData?.data?.firstName} ${profileProfessionalData?.data?.lastName}",
                userState = userState
            )
            registerProfessional(user)
        } else {
            Log.e("FirebaseRegister", "Please fill all fields")
        }
    }
    private fun registerProfessional(user: RegisterUserInFirebaseRequest) {
        val database: DatabaseReference = FirebaseDatabase.getInstance().reference
        database.child("Users").child(user.uid).setValue(user)
            .addOnSuccessListener {
                Log.d("FirebaseRegister", "Professional registered successfully!")
            }
            .addOnFailureListener { exception ->
                Log.e("FirebaseRegister", "Error registering user: ${exception.message}")
            }
    }

    private fun initObserver() {
        viewModel.resultProfileProfessionalDetailAccount.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        MyApplication.showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        MyApplication.hideProgress()
                        AppPrefs(requireContext()).setProfileProfessionalData("PROFILE_DATA",it.data)
                      //  binding.tvName.text ="${it.data?.data?.firstName} ${it.data?.data?.lastName}"
                        //binding.tvPhone.text =it.data?.data?.phoneNumber
                        val name = "${it.data?.data?.firstName} ${it.data?.data?.lastName}"
                        val email = it.data?.data?.email.toString()
                        if (!it.data?.data?.professionalDetail?.professionalDetailId.isNullOrEmpty()){
                            AppPrefs(requireContext()).saveStringPref(PROFESSIONAL_DETAIL_ID,it.data?.data?.professionalDetail?.professionalDetailId) }
                        profileUpdatedListener.onProfileUpdated(name, email,BASE_FILE +it.data?.data?.profilepic)
                        Glide.with(requireContext()).load(BASE_FILE +it.data?.data?.profilepic).into(binding.ivProfile)

                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        MyApplication.hideProgress()
                    }
                }
            }
        }
    }
    private fun initUI() {
        binding.ivDrawer.setOnClickListener(this)
        (activity as ProfessionalActivity).bottomNavigation(true)
        viewModel.getProfessionalDetail()
        binding.tlAppointment.removeAllTabs()
        binding.tlAppointment.addTab(binding.tlAppointment.newTab().setText("UPCOMING"))
        binding.tlAppointment.addTab(binding.tlAppointment.newTab().setText("CONFIRMED"))
        binding.tlAppointment.addTab(binding.tlAppointment.newTab().setText("PAST"))

        binding.tlAppointment.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> setUpComingAdapter()
                    1 -> setConfirmedAdapter()
                    2 -> setPastAdapter()
                }
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {

            }

            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

        })

        setUpComingAdapter()
    }

    private fun setConfirmedAdapter() {
        binding.rvAppointment.adapter = ConfirmedAppointmentAdapter()
    }

    private fun setUpComingAdapter() {
        binding.rvAppointment.adapter = UpcomingAppointmentAdapter()
    }

    private fun setPastAdapter() {
        binding.rvAppointment.adapter = PastAppointmentAdapter()
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.ivDrawer -> {
                (activity as ProfessionalActivity).isDrawer(true)
            }
        }


    }

    interface OnProfileUpdatedListener {
        fun onProfileUpdated(name: String, email: String,profileImage:String)
    }
}