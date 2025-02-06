package com.app.gentlemanspa.ui.professionalDashboard.fragment.chat.chat.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.gentlemanspa.databinding.ItemMyMessageBinding
import com.app.gentlemanspa.ui.customerDashboard.fragment.chat.chat.model.CustomerChatHistoryMessage
import com.app.gentlemanspa.utils.AppPrefs
import com.app.gentlemanspa.utils.PROFESSIONAL_USER_ID
import com.app.gentlemanspa.utils.setGone
import com.app.gentlemanspa.utils.utcToCurrentDateTimeFormat


class ProfessionalChatHistoryAdapter (val context:Context, private val chatHistoryList:List<CustomerChatHistoryMessage>) : RecyclerView.Adapter<ProfessionalChatHistoryAdapter.MessageViewHolder>() {

    private lateinit var professionalChatCallBacks:ProfessionalChatCallBacks

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
            if (AppPrefs(context).getStringPref(PROFESSIONAL_USER_ID)
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
                professionalChatCallBacks.onMessageItemClick(item.id)
               true
            }
            tvReceiverMessage.setOnLongClickListener{
                professionalChatCallBacks.onMessageItemClick(item.id)
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

    fun setProfessionalChatCallBacks(onClick:ProfessionalChatCallBacks){
        professionalChatCallBacks=onClick
    }

   interface ProfessionalChatCallBacks{
       fun onMessageItemClick(messageId:Int)
   }
}