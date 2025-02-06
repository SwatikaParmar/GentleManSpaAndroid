package com.app.gentlemanspa.ui.customerDashboard.fragment.chat.chat

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
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
        binding.tvName.text=args.name
        Glide.with(requireContext()).load(ApiConstants.BASE_FILE +args.profilePic).placeholder(
            R.drawable.profile_placeholder).error(R.drawable.profile_placeholder).into(binding.ivProfile)

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
    private fun callCustomerChatHistoryApi() {
        viewModel.senderId.set(args.messageSenderId)
        viewModel.receiverId.set(args.messageReceiverId)
        viewModel.getCustomerChatHistoryApi()
    }
    override fun onClick(v: View?) {
        when (v) {
            binding.ivArrowBack -> {
                findNavController().popBackStack()

            }

            binding.ivSendMessage -> {
                if (binding.etMessage.text!!.isNotBlank()){
                    proceedToSendMessage(binding.etMessage.text.toString().trim())
                }
            }
        }
    }
    private fun proceedToSendMessage(messageContent:String) {
        binding.etMessage.text!!.clear()
        val request = CustomerSendMessageRequest(
            0,
            messageContent,
            "text",
            args.messageReceiverId,
            args.messageSenderId
        )
        viewModel.customerSendMessageApi(request)

    }
    private fun setCustomerChatHistoryAdapter(chatHistoryList: List<CustomerChatHistoryMessage>) {
        val customerChatHistoryAdapter =
            CustomerChatHistoryAdapter(requireContext(), chatHistoryList)
        binding.rvChatHistory.adapter = customerChatHistoryAdapter
        binding.rvChatHistory.post {
            binding.rvChatHistory.scrollToPosition(customerChatHistoryAdapter.itemCount - 1)
        }

        customerChatHistoryAdapter.setCustomerChatCallBacks(object :CustomerChatHistoryAdapter.CustomerChatCallBacks{
            override fun onMessageItemClick(messageId: Int) {
                Log.d("MessageId","MessageId->$messageId")
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
                        Log.d("chatHistory", "data->${it.data!!.data.messages}")
                        Log.d("chatHistory", "senderOnlineStatus->${it.data.data.receiverOnlineStatus}")
                        if (it.data.data.receiverOnlineStatus){
                            binding.tvStatus.setVisible()
                            binding.tvStatus.text=getString(R.string.online)
                        }else{
                            binding.tvStatus.setGone()
                        }
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

