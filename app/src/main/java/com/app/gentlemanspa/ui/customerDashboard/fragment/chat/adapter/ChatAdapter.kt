package com.app.gentlemanspa.ui.customerDashboard.fragment.chat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.app.gentlemanspa.R
import com.app.gentlemanspa.databinding.ItemChatReceivedBinding
import com.app.gentlemanspa.databinding.ItemChatSentBinding
import com.app.gentlemanspa.ui.customerDashboard.fragment.chat.model.ChatMessage
import com.app.gentlemanspa.ui.customerDashboard.fragment.chat.model.MessageType

class ChatAdapter(private val messages: List<ChatMessage>) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    // ViewHolder using ViewBinding
    inner class ChatViewHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val binding = if (viewType == MessageType.SENT.ordinal) {
            // Inflate the specific binding class for SENT messages
            ItemChatSentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        } else {
            // Inflate the specific binding class for RECEIVED messages
            ItemChatReceivedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        }
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chatMessage = messages[position]

        // Use the ViewBinding to access views
        if (holder.binding is ItemChatSentBinding) {
            // Handle the SENT message view
            holder.binding.messageTextView.text = chatMessage.message
            holder.binding.timestampTextView.text = chatMessage.timestamp
            holder.binding.messageTextView.setBackgroundResource(R.drawable.bg_message_sent)
        } else if (holder.binding is ItemChatReceivedBinding) {
            // Handle the RECEIVED message view
            holder.binding.messageTextView.text = chatMessage.message
            holder.binding.timestampTextView.text = chatMessage.timestamp
            holder.binding.messageTextView.setBackgroundResource(R.drawable.bg_message_received)

      }
    }

    override fun getItemViewType(position: Int): Int {
        return messages[position].messageType.ordinal
    }

    override fun getItemCount(): Int {
        return messages.size
    }
}
