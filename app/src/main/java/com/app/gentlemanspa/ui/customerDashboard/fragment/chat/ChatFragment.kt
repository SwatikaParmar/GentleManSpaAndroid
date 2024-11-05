package com.app.gentlemanspa.ui.customerDashboard.fragment.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.app.gentlemanspa.databinding.FragmentChatBinding
import com.app.gentlemanspa.ui.customerDashboard.fragment.chat.adapter.ChatAdapter
import com.app.gentlemanspa.ui.customerDashboard.fragment.chat.model.ChatMessage
import com.app.gentlemanspa.ui.customerDashboard.fragment.chat.model.MessageType
import com.app.gentlemanspa.utils.getCurrentTime


class ChatFragment : Fragment(), View.OnClickListener {
    lateinit var binding: FragmentChatBinding
    private val chatMessages = mutableListOf<ChatMessage>()
    private lateinit var chatAdapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
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

    private fun initUI() {
        binding.onClick = this
        setChatAdapter()

    }

    private fun setChatAdapter() {

        chatAdapter = ChatAdapter(chatMessages)
        binding.rvChat.adapter = chatAdapter
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.ivArrowBack->{
                findNavController().popBackStack()
            }
            binding.ivMessageSend -> {
                doChat()
            }
        }
    }

    private fun doChat() {
        val messageText = binding.editTextMessage.text.toString().trim()
        if (messageText.isNotEmpty()) {
            // Add sent message to the list
            val sentMessage = ChatMessage(
                sender = "You",  // Example sender name
                message = messageText,
                timestamp = getCurrentTime(),
                messageType = MessageType.SENT
            )
            chatMessages.add(sentMessage)

            // Add received message (for demo purposes)
            val receivedMessage = ChatMessage(
                sender = "John",  // Example receiver name
                message = "Hi! How are you?",
                timestamp = getCurrentTime(),
                messageType = MessageType.RECEIVED
            )
            chatMessages.add(receivedMessage)

            // Notify adapter of new messages
            chatAdapter.notifyDataSetChanged()

            // Scroll RecyclerView to the bottom
            binding.rvChat.scrollToPosition(chatMessages.size - 1)

            // Clear input field
            binding.editTextMessage.text!!.clear()
        }
    }
}