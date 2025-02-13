package com.app.gentlemanspa.ui.customerDashboard.fragment.chat.chat

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.app.gentlemanspa.R
import com.app.gentlemanspa.base.MyApplication.Companion.hideProgress
import com.app.gentlemanspa.databinding.FragmentCustomerChatBinding
import com.app.gentlemanspa.network.ApiConstants
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.network.Status
import com.app.gentlemanspa.ui.customerDashboard.activity.CustomerActivity
import com.app.gentlemanspa.ui.customerDashboard.fragment.chat.chat.adapter.CustomerChatHistoryAdapter
import com.app.gentlemanspa.ui.customerDashboard.fragment.chat.chat.model.CustomerChatHistoryMessage
import com.app.gentlemanspa.ui.customerDashboard.fragment.chat.chat.model.CustomerSendMessageRequest
import com.app.gentlemanspa.ui.customerDashboard.fragment.chat.chat.viewModel.CustomerChatViewModel
import com.app.gentlemanspa.utils.AppPrefs
import com.app.gentlemanspa.utils.CUSTOMER_USER_ID
import com.app.gentlemanspa.utils.ViewModelFactory
import com.app.gentlemanspa.utils.setGone
import com.app.gentlemanspa.utils.setVisible
import com.app.gentlemanspa.utils.showMessageOptionsDialog
import com.app.gentlemanspa.utils.showToast
import com.bumptech.glide.Glide

class CustomerChatFragment : Fragment(), View.OnClickListener {

    lateinit var binding: FragmentCustomerChatBinding
    private val args: CustomerChatFragmentArgs by navArgs()
    private lateinit var handler: Handler
    private lateinit var updateRunnable: Runnable
    private val viewModel: CustomerChatViewModel by viewModels {
        ViewModelFactory(
            InitialRepository()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObserver()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        if (!this::binding.isInitialized) {
            binding = FragmentCustomerChatBinding.inflate(layoutInflater, container, false)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        (activity as CustomerActivity).bottomNavigation(false)
        binding.onClick = this
        callCustomerChatHistoryApi()
        handler = Handler(Looper.getMainLooper())
        updateRunnable = object : Runnable {
            override fun run() {
                callCustomerChatHistoryApi()
                handler.postDelayed(this, 7000) // Repeat every 5 seconds
            }
        }
        handler.postDelayed(updateRunnable, 7000)
    }
    override fun onPause() {
        handler.removeCallbacks(updateRunnable)
        super.onPause()
    }

    private fun callCustomerChatHistoryApi() {
        Log.d("test", "messageSenderId->${args.messageSenderId}")
        Log.d(
            "test", "CUSTOMER_USER_ID->${
                AppPrefs(requireActivity()).getStringPref(CUSTOMER_USER_ID)
                    .toString()
            }"
        )
        viewModel.senderId.set(AppPrefs(requireActivity()).getStringPref(CUSTOMER_USER_ID)
            .toString())
        // viewModel.receiverId.set(args.messageReceiverId)
        viewModel.receiverId.set(args.messageSenderId)
        viewModel.getCustomerChatHistoryApi()
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.ivArrowBack -> {
                if (args.from.isNotEmpty() && args.from =="notification") {
                    Log.d("test","inside if part of back")
                    val action =
                        CustomerChatFragmentDirections.actionCustomerChatFragmentToHomeCustomerFragment()
                    val navOptions =
                        NavOptions.Builder().setPopUpTo(R.id.customerChatFragment, true).build()
                    findNavController().navigate(action, navOptions)
                } else {
                    Log.d("test","inside else part of back")
                    findNavController().popBackStack()

                }

            }

            binding.ivSendMessage -> {
                if (binding.etMessage.text!!.isNotBlank()) {
                    proceedToSendMessage(binding.etMessage.text.toString().trim())
                }
            }
        }
    }

    private fun proceedToSendMessage(messageContent: String) {
        binding.etMessage.text!!.clear()
        val request = CustomerSendMessageRequest(
            0,
            messageContent,
            "text",
            args.messageSenderId,
            AppPrefs(requireContext()).getStringPref(CUSTOMER_USER_ID).toString())
        Log.d("SendMessage","request->${request}")
        viewModel.customerSendMessageApi(request)

    }

    private fun setCustomerChatHistoryAdapter(chatHistoryList: List<CustomerChatHistoryMessage>) {
        val customerChatHistoryAdapter =
            CustomerChatHistoryAdapter(requireContext(), chatHistoryList)
        binding.rvChatHistory.adapter = customerChatHistoryAdapter
        binding.rvChatHistory.post {
            binding.rvChatHistory.scrollToPosition(customerChatHistoryAdapter.itemCount - 1)
        }

        customerChatHistoryAdapter.setCustomerChatCallBacks(object :
            CustomerChatHistoryAdapter.CustomerChatCallBacks {
            override fun onMessageItemClick(messageId: Int) {
                Log.d("MessageId", "MessageId->$messageId")
                showMessageOptionsDialog(requireContext(), "Delete this message?") {
                    viewModel.messageId.set(messageId)
                    viewModel.deleteMessageApi()
                }
            }

        })
    }

    private fun initObserver() {
        viewModel.resultCustomerChatHistoryList.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        // showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        hideProgress()
                        //     requireContext().showToast(it.data!!.messages)
                        Log.d("chatHistory", "data->${it.data!!.data}")
                        Log.d(
                            "chatHistory",
                            "senderOnlineStatus->${it.data.data.receiverOnlineStatus}"
                        )
                        if (it.data.data.receiverOnlineStatus) {
                            binding.tvStatus.setVisible()
                            binding.tvStatus.text = getString(R.string.online)
                        } else {
                            binding.tvStatus.setGone()
                        }
                        binding.tvName.text = it.data.data.name
                        Glide.with(requireContext())
                            .load(ApiConstants.BASE_FILE + it.data.data.senderProfilePic)
                            .placeholder(
                                R.drawable.profile_placeholder
                            ).error(R.drawable.profile_placeholder).into(binding.ivProfile)
                        setCustomerChatHistoryAdapter(it.data.data.messages)

                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        hideProgress()
                    }
                }
            }
        }
        viewModel.resultCustomerSendMessage.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        //    showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        hideProgress()
                        if (it.data!!.isSuccess) {
                            callCustomerChatHistoryApi()
                        } else {
                            requireContext().showToast(it.data.messages)
                        }
                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        hideProgress()
                    }
                }
            }
        }
        viewModel.resultDeleteMessage.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        //    showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        hideProgress()
                        if (it.data!!.isSuccess) {
                            callCustomerChatHistoryApi()
                        } else {
                            requireContext().showToast(it.data.messages)
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

