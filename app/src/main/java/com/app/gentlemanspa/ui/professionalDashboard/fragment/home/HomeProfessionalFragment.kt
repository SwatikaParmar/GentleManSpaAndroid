package com.app.gentlemanspa.ui.professionalDashboard.fragment.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.gentlemanspa.base.MyApplication
import com.app.gentlemanspa.base.MyApplication.Companion.hideProgress
import com.app.gentlemanspa.databinding.FragmentHomeProfessionalBinding
import com.app.gentlemanspa.network.ApiConstants.BASE_FILE
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.network.Status
import com.app.gentlemanspa.ui.customerDashboard.fragment.history.HistoryCustomerFragmentDirections
import com.app.gentlemanspa.ui.customerDashboard.fragment.history.model.UpcomingServiceAppointmentItem
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.model.RegisterUserInFirebaseRequest
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.model.UserState
import com.app.gentlemanspa.ui.professionalDashboard.activity.ProfessionalActivity
import com.app.gentlemanspa.ui.professionalDashboard.fragment.home.adapter.CompletedAppointmentAdapter
import com.app.gentlemanspa.ui.professionalDashboard.fragment.home.adapter.CancelledAppointmentAdapter
import com.app.gentlemanspa.ui.professionalDashboard.fragment.home.adapter.UpcomingAppointmentAdapter
import com.app.gentlemanspa.ui.professionalDashboard.fragment.home.viewModel.HomeProfessionalViewModel
import com.app.gentlemanspa.utils.AppPrefs
import com.app.gentlemanspa.utils.FCM_TOKEN
import com.app.gentlemanspa.utils.PROFESSIONAL_DETAIL_ID
import com.app.gentlemanspa.utils.PROFESSIONAL_PROFILE_DATA
import com.app.gentlemanspa.utils.PROFESSIONAL_USER_ID
import com.app.gentlemanspa.utils.ViewModelFactory
import com.app.gentlemanspa.utils.getCustomerCurrentDate
import com.app.gentlemanspa.utils.getCustomerCurrentTime
import com.app.gentlemanspa.utils.showToast
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HomeProfessionalFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentHomeProfessionalBinding
    private lateinit var profileUpdatedListener: OnProfileUpdatedListener
    private var appointmentType = ""
    private val appointmentsList: ArrayList<UpcomingServiceAppointmentItem> = ArrayList()

    private val viewModel: HomeProfessionalViewModel by viewModels {
        ViewModelFactory(
            InitialRepository()
        )
    }

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
        //  registerProfessionalInFirebase()

    }

    private fun callAppointmentsListApi(type: String) {
        Log.d("type", "type->$type")
        appointmentType = type
        viewModel.type.set(type)
        viewModel.getAppointmentListApi()
    }

    private fun registerProfessionalInFirebase() {
        Log.e(
            "FirebaseRegister",
            "${AppPrefs(requireContext()).getStringPref(PROFESSIONAL_USER_ID)}"
        )

        val profileProfessionalData =
            AppPrefs(requireContext()).getProfileProfessionalData(PROFESSIONAL_PROFILE_DATA)
        if (profileProfessionalData?.data?.firstName.toString()
                .isNotEmpty() && profileProfessionalData?.data?.email.toString()
                .isNotEmpty() && profileProfessionalData?.data?.gender.toString().isNotEmpty()
        ) {
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
                        AppPrefs(requireContext()).setProfileProfessionalData(
                            PROFESSIONAL_PROFILE_DATA,
                            it.data
                        )
                        registerProfessionalInFirebase()
                        val name = "${it.data?.data?.firstName} ${it.data?.data?.lastName}"
                        val email = it.data?.data?.email.toString()
                        if (!it.data?.data?.professionalDetail?.professionalDetailId.isNullOrEmpty()) {
                            AppPrefs(requireContext()).saveStringPref(
                                PROFESSIONAL_DETAIL_ID,
                                it.data?.data?.professionalDetail?.professionalDetailId
                            )
                        }
                        profileUpdatedListener.onProfileUpdated(
                            name,
                            email,
                            BASE_FILE + it.data?.data?.profilepic
                        )
                        //   Glide.with(requireContext()).load(BASE_FILE +it.data?.data?.profilepic).into(binding.ivProfile)

                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        MyApplication.hideProgress()
                    }
                }
            }
        }

        viewModel.resultAppointmentList.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        // showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        hideProgress()
                        //   requireContext().showToast(it.message.toString())
                        appointmentsList.clear()
                        it.data?.data?.dataList?.let { it1 -> appointmentsList.addAll(it1) }
                        when (appointmentType) {
                            "Upcoming" -> {
                                setUpComingAdapter()
                            }

                            "Completed" -> {
                                setUpCompletedAdapter()
                            }

                            "Cancelled" -> {
                                setUpCancelledAdapter()
                            }

                            else -> {
                                requireContext().showToast("Something went wrong while fetching appointment")
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

    }

    private fun initUI() {
        (activity as ProfessionalActivity).bottomNavigation(true)
        binding.onClick = this
        viewModel.getProfessionalDetail()
        binding.tlAppointment.removeAllTabs()
        binding.tlAppointment.addTab(binding.tlAppointment.newTab().setText("Upcoming"))
        binding.tlAppointment.addTab(binding.tlAppointment.newTab().setText("Completed"))
        binding.tlAppointment.addTab(binding.tlAppointment.newTab().setText("Cancelled"))
        binding.tlAppointment.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        viewModel.professionalDetailId.set(
                            AppPrefs(requireContext()).getStringPref(
                                PROFESSIONAL_DETAIL_ID
                            )?.toInt()
                        )
                        callAppointmentsListApi("Upcoming")
                    }

                    1 -> {
                        viewModel.professionalDetailId.set(
                            AppPrefs(requireContext()).getStringPref(
                                PROFESSIONAL_DETAIL_ID
                            )?.toInt()
                        )
                        callAppointmentsListApi("Completed")
                    }

                    2 -> {
                        viewModel.professionalDetailId.set(
                            AppPrefs(requireContext()).getStringPref(
                                PROFESSIONAL_DETAIL_ID
                            )?.toInt()
                        )
                        callAppointmentsListApi("Cancelled")
                    }
                }
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {

            }

            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

        })
        if (AppPrefs(requireContext()).getStringPref(
                PROFESSIONAL_DETAIL_ID
            ).toString().isNotEmpty()){
            viewModel.professionalDetailId.set(
                AppPrefs(requireContext()).getStringPref(
                    PROFESSIONAL_DETAIL_ID
                )?.toInt()
            )
            callAppointmentsListApi("Upcoming")
        }else{
            Log.d("professionalDetailId","professionalDetailId is empty")
        }
    }

    private fun setUpCompletedAdapter() {
        binding.rvAppointment.adapter = CompletedAppointmentAdapter(appointmentsList)
    }

    private fun setUpComingAdapter() {
        val upcomingAppointmentAdapter = UpcomingAppointmentAdapter(appointmentsList)
        binding.rvAppointment.adapter = upcomingAppointmentAdapter
        upcomingAppointmentAdapter.setUpcomingAppointmentCallbacks(object :
            UpcomingAppointmentAdapter.UpcomingAppointmentCallbacks {
            override fun onItemMessageClick(item: UpcomingServiceAppointmentItem) {
                checkUserExistsAndNavigateToChat(item)
            }

        })

    }

    private fun setUpCancelledAdapter() {
        binding.rvAppointment.adapter = CancelledAppointmentAdapter(appointmentsList)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.ivDrawer -> {
                (activity as ProfessionalActivity).isDrawer(true)
            }

            binding.ivMessages -> {
                val professionalUserId = "${
                    AppPrefs(requireContext()).getStringPref(
                        PROFESSIONAL_USER_ID
                    )
                }"
                val action =
                    HomeProfessionalFragmentDirections.actionHomeProfessionalFragmentToProfessionalMessageFragment(
                        professionalUserId
                    )
                findNavController().navigate(action)


            }
        }
    }

    fun checkUserExistsAndNavigateToChat(item: UpcomingServiceAppointmentItem) {
        Log.d(
            "sendMessage",
            "sendMessage userId->${item.userId} professionalUserId->${item.professionalUserId}"
        )

        if (item.userId.isNotEmpty() && item.professionalUserId.isNotEmpty()) {
            FirebaseDatabase.getInstance().reference
                .child("Users").child(item.professionalUserId)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        Log.d(
                            "sendMessage",
                            "sendMessage snapshot exists: ${snapshot.exists()} "
                        )
                        if (snapshot.exists()) {
                            val action =
                                HomeProfessionalFragmentDirections.actionHomeProfessionalFragmentToProfessionalChatFragment(
                                    item.professionalUserId,
                                    item.userId,
                                    "HomeProfessionalFragment"
                                )
                            findNavController().navigate(action)
                            Log.d(
                                "sendMessage",
                                "sendMessage inside if : ${snapshot.exists()} "
                            )
                        } else {
                            Log.d(
                                "sendMessage",
                                "sendMessage inside else: ${snapshot.exists()} "
                            )
                            requireContext().showToast("This user is unavailable for chat!")
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        requireContext().showToast(error.message)
                    }
                })
        }
    }

    interface OnProfileUpdatedListener {
        fun onProfileUpdated(name: String, email: String, profileImage: String)
    }

}