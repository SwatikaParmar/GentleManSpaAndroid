package com.app.gentlemanspa.ui.professionalDashboard.fragment.chat.chat

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
import com.app.gentlemanspa.databinding.FragmentChatBinding
import com.app.gentlemanspa.network.ApiConstants
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.network.Status
import com.app.gentlemanspa.ui.customerDashboard.fragment.chat.chat.model.CustomerChatHistoryMessage
import com.app.gentlemanspa.ui.customerDashboard.fragment.chat.chat.model.CustomerSendMessageRequest
import com.app.gentlemanspa.ui.professionalDashboard.activity.ProfessionalActivity
import com.app.gentlemanspa.ui.professionalDashboard.fragment.chat.chat.adapter.ProfessionalChatHistoryAdapter
import com.app.gentlemanspa.ui.professionalDashboard.fragment.chat.chat.viewModel.ProfessionalChatViewModel
import com.app.gentlemanspa.utils.AppPrefs
import com.app.gentlemanspa.utils.CUSTOMER_USER_ID
import com.app.gentlemanspa.utils.PROFESSIONAL_USER_ID
import com.app.gentlemanspa.utils.ViewModelFactory
import com.app.gentlemanspa.utils.setGone
import com.app.gentlemanspa.utils.setVisible
import com.app.gentlemanspa.utils.showMessageOptionsDialog
import com.app.gentlemanspa.utils.showToast
import com.bumptech.glide.Glide


class ProfessionalChatFragment : Fragment(), View.OnClickListener {
    lateinit var binding: FragmentChatBinding
    private val args: ProfessionalChatFragmentArgs by navArgs()
    private lateinit var handler: Handler
    private lateinit var updateRunnable: Runnable

    private val viewModel: ProfessionalChatViewModel by viewModels {
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
            binding = FragmentChatBinding.inflate(layoutInflater, container, false)
        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun callProfessionalChatHistoryApi() {
        Log.d("test", "messageSenderId->${args.messageSenderId}")
        Log.d(
            "test", "CUSTOMER_USER_ID->${
                AppPrefs(requireActivity()).getStringPref(PROFESSIONAL_USER_ID)
                    .toString()
            }"
        )
        viewModel.senderId.set(
            AppPrefs(requireActivity()).getStringPref(PROFESSIONAL_USER_ID)
                .toString()
        )
        viewModel.receiverId.set(args.messageSenderId)
        viewModel.getCustomerChatHistoryApi()
    }


    private fun initUI() {
        (activity as ProfessionalActivity).bottomNavigation(false)
        binding.onClick = this
        callProfessionalChatHistoryApi()
        handler = Handler(Looper.getMainLooper())
        updateRunnable = object : Runnable {
            override fun run() {
                callProfessionalChatHistoryApi()
                handler.postDelayed(this, 7000) // Repeat every 5 seconds
            }
        }
        handler.postDelayed(updateRunnable, 7000)

    }

    override fun onPause() {
        handler.removeCallbacks(updateRunnable)
        super.onPause()
    }
    override fun onClick(v: View?) {
        when (v) {
            binding.ivArrowBack -> {
                findNavController().popBackStack()

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
            AppPrefs(requireContext()).getStringPref(PROFESSIONAL_USER_ID).toString()
        )
        Log.d("SendMessage","request->${request}")
        viewModel.customerSendMessageApi(request)

    }

    private fun setCustomerChatHistoryAdapter(chatHistoryList: List<CustomerChatHistoryMessage>) {
        val professionalChatHistoryAdapter =
            ProfessionalChatHistoryAdapter(requireContext(), chatHistoryList)
        binding.rvChatHistory.adapter = professionalChatHistoryAdapter
        binding.rvChatHistory.post {
            binding.rvChatHistory.scrollToPosition(professionalChatHistoryAdapter.itemCount - 1)
        }

        professionalChatHistoryAdapter.setProfessionalChatCallBacks(object :
            ProfessionalChatHistoryAdapter.ProfessionalChatCallBacks {
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
                        Log.d("chatHistory", "data->${it.data!!.data.messages}")
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
                        if (it.data.data.messages.isNotEmpty()) {
                            setCustomerChatHistoryAdapter(it.data.data.messages)
                        }

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
                            callProfessionalChatHistoryApi()
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
                            callProfessionalChatHistoryApi()
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