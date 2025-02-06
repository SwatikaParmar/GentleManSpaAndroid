package com.app.gentlemanspa.ui.professionalDashboard.fragment.chat.messages

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.gentlemanspa.base.MyApplication.Companion.hideProgress
import com.app.gentlemanspa.databinding.DialogRemoveUserChatBinding
import com.app.gentlemanspa.databinding.FragmentProfessionalMessagesBinding
import com.app.gentlemanspa.databinding.ItemMessagesBinding
import com.app.gentlemanspa.network.ApiConstants
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.network.Status
import com.app.gentlemanspa.ui.customerDashboard.fragment.chat.messages.CustomerMessagesFragmentDirections
import com.app.gentlemanspa.ui.customerDashboard.fragment.chat.messages.adapter.CustomerMessagesAdapter
import com.app.gentlemanspa.ui.customerDashboard.fragment.chat.messages.model.CustomerMessagesData
import com.app.gentlemanspa.ui.professionalDashboard.activity.ProfessionalActivity
import com.app.gentlemanspa.ui.professionalDashboard.fragment.chat.messages.adapter.ProfessionalMessagesAdapter
import com.app.gentlemanspa.ui.professionalDashboard.fragment.chat.messages.viewModel.ProfessionalMessagesViewModel
import com.app.gentlemanspa.utils.AppPrefs
import com.app.gentlemanspa.utils.CUSTOMER_USER_ID
import com.app.gentlemanspa.utils.CommonUtils
import com.app.gentlemanspa.utils.PROFESSIONAL_USER_ID
import com.app.gentlemanspa.utils.ViewModelFactory
import com.app.gentlemanspa.utils.setGone
import com.app.gentlemanspa.utils.setVisible
import com.app.gentlemanspa.utils.showToast
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ProfessionalMessageFragment : Fragment(), View.OnClickListener {
  lateinit var binding:FragmentProfessionalMessagesBinding
    private val viewModel: ProfessionalMessagesViewModel by viewModels {
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
            binding=FragmentProfessionalMessagesBinding.inflate(layoutInflater,container,false)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        (activity as ProfessionalActivity).bottomNavigation(false)
        binding.onClick=this
        Log.d("UserId","UserId->${AppPrefs(requireContext()).getStringPref(PROFESSIONAL_USER_ID)}")
        viewModel.userId.set(AppPrefs(requireContext()).getStringPref(PROFESSIONAL_USER_ID).toString())
        viewModel.getCustomerMessagesListApi()

    }

    override fun onClick(v: View?) {
        when(v){
            binding.ivArrowBack->{
                findNavController().popBackStack()
            }
        }
    }

    private fun setProfessionalMessagesAdapter(customerMessagesList: List<CustomerMessagesData>) {
        val professionalMessagesAdapter= ProfessionalMessagesAdapter(customerMessagesList)
        binding.rvProfessionalMessages.adapter=professionalMessagesAdapter
        professionalMessagesAdapter.setProfessionalMessagesCallbacks(object: ProfessionalMessagesAdapter.ProfessionalMessagesCallbacks{
            override fun rootProfessionalMessages(item: CustomerMessagesData) {
                Log.d("userId","receiverId->${item.userId}")
                Log.d("userId","userName->${item.userName}")
                val messageSenderId= AppPrefs(requireContext()).getStringPref(PROFESSIONAL_USER_ID).toString()
                val messageReceiverId=item.userName
                val name="${item.firstName} ${item.lastName}"
                val profilePic=item.profilePic
                val action= ProfessionalMessageFragmentDirections.actionProfessionalMessageFragmentToProfessionalChatFragment(
                    messageSenderId, messageReceiverId,name,profilePic)
                findNavController().navigate(action)
            }
        })

    }
    private fun initObserver() {
        viewModel.resultCustomerMessagesList.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        //  showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        hideProgress()
                        Log.d("messages","inside success")

                        if (it.data?.data!!.isNotEmpty()){
                            binding.clNoUserExist.setGone()
                            setProfessionalMessagesAdapter(it.data.data)
                        }else{
                            binding.clNoUserExist.setVisible()
                        }
                    }

                    Status.ERROR -> {
                        Log.d("messages","inside error${it.message}")
                        requireContext().showToast(it.message.toString())
                        hideProgress()
                    }
                }
            }
        }
    }


}