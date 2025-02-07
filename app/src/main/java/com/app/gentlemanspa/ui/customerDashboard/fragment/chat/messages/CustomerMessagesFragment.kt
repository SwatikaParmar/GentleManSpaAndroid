package com.app.gentlemanspa.ui.customerDashboard.fragment.chat.messages

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.gentlemanspa.base.MyApplication.Companion.hideProgress
import com.app.gentlemanspa.databinding.FragmentCustomerMessageBinding
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.network.Status
import com.app.gentlemanspa.ui.customerDashboard.activity.CustomerActivity
import com.app.gentlemanspa.ui.customerDashboard.fragment.chat.messages.adapter.CustomerMessagesAdapter
import com.app.gentlemanspa.ui.customerDashboard.fragment.chat.messages.model.CustomerMessagesData
import com.app.gentlemanspa.ui.customerDashboard.fragment.chat.messages.viewModel.CustomerMessagesViewModel
import com.app.gentlemanspa.utils.AppPrefs
import com.app.gentlemanspa.utils.CUSTOMER_USER_ID
import com.app.gentlemanspa.utils.ViewModelFactory
import com.app.gentlemanspa.utils.setGone
import com.app.gentlemanspa.utils.setVisible
import com.app.gentlemanspa.utils.showToast

class CustomerMessagesFragment : Fragment(), View.OnClickListener {
    lateinit var binding: FragmentCustomerMessageBinding
    private val viewModel: CustomerMessagesViewModel by viewModels {
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
            binding=FragmentCustomerMessageBinding.inflate(layoutInflater,container,false)
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
        Log.d("UserId","UserId->${AppPrefs(requireContext()).getStringPref(CUSTOMER_USER_ID)}")
        viewModel.userId.set(AppPrefs(requireContext()).getStringPref(CUSTOMER_USER_ID).toString())
        viewModel.getCustomerMessagesListApi()
    }

    override fun onClick(v: View?) {
        when(v){
            binding.ivArrowBack->{
               findNavController().popBackStack()
            }
        }
    }
    private fun setCustomerMessagesAdapter(customerMessagesList: List<CustomerMessagesData>) {
        val customerMessagesAdapter=CustomerMessagesAdapter(customerMessagesList)
        binding.rvCustomerChatUsers.adapter=customerMessagesAdapter
        customerMessagesAdapter.setCustomerMessagesCallbacks(object:CustomerMessagesAdapter.CustomerMessagesCallbacks{
            override fun rootCustomerMessages(item: CustomerMessagesData) {
                Log.d("userId","receiverId->${item.userId}")
                Log.d("userId","userName->${item.userName}")
                val messageSenderId= AppPrefs(requireContext()).getStringPref(CUSTOMER_USER_ID).toString()
                val messageReceiverId=item.userName
                val name="${item.firstName} ${item.lastName}"
                val profilePic=item.profilePic?:""
                val action=CustomerMessagesFragmentDirections.actionCustomerMessageFragmentToCustomerChatFragment(
                    messageSenderId,messageReceiverId,name,profilePic
                )
                findNavController().navigate(action)
            }
        })

    }
    private fun initObserver() {
        viewModel.resultCustomerMessagesList.observe(this) { it ->
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                      //  showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        hideProgress()
                        if (it.data?.data!!.isNotEmpty()){
                            binding.clNoUserExist.setGone()
                            val sortedMessages = it.data.data.sortedByDescending { it.lastMessageTime }
                            setCustomerMessagesAdapter(sortedMessages)
                        }else{
                            binding.clNoUserExist.setVisible()
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