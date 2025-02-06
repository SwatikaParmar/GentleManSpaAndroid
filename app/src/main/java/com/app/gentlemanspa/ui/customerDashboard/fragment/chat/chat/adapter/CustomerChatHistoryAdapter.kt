package com.app.gentlemanspa.ui.customerDashboard.fragment.chat.chat.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.gentlemanspa.databinding.ItemMyMessageBinding
import com.app.gentlemanspa.ui.customerDashboard.fragment.chat.chat.model.CustomerChatHistoryMessage
import com.app.gentlemanspa.utils.AppPrefs
import com.app.gentlemanspa.utils.CUSTOMER_USER_ID
import com.app.gentlemanspa.utils.utcToCurrentDateTimeFormat
import com.app.gentlemanspa.utils.setGone

class CustomerChatHistoryAdapter (val context:Context, private val chatHistoryList:List<CustomerChatHistoryMessage>) : RecyclerView.Adapter<CustomerChatHistoryAdapter.MessageViewHolder>() {

    private lateinit var customerChatCallBacks:CustomerChatCallBacks

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder(
            ItemMyMessageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val item = chatHistoryList[position]
        holder.binding.apply {
            if (AppPrefs(context).getStringPref(CUSTOMER_USER_ID)
                    .toString() == item.senderUserName
            ) {
                llMessageReceiver.setGone()
                tvReceiverTime.setGone()
                tvSenderMessage.text = item.messageContent
                tvSenderTime.text = utcToCurrentDateTimeFormat(item.timestamp)

            } else {
                llMessageSender.setGone()
                tvSenderTime.setGone()
                tvReceiverMessage.text = item.messageContent
                tvReceiverTime.text = utcToCurrentDateTimeFormat(item.timestamp)

            }
            tvSenderMessage.setOnLongClickListener{
                customerChatCallBacks.onMessageItemClick(item.id)
                true
            }
            tvReceiverMessage.setOnLongClickListener{
                customerChatCallBacks.onMessageItemClick(item.id)
                true
            }

        }


    }

    override fun getItemCount(): Int {
        return chatHistoryList.size
    }

    class MessageViewHolder(itemView: ItemMyMessageBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val binding: ItemMyMessageBinding = itemView
    }

    fun setCustomerChatCallBacks(onClick:CustomerChatCallBacks){
        customerChatCallBacks=onClick
    }

    interface CustomerChatCallBacks{
        fun onMessageItemClick(messageId:Int)
    }

}

