package com.app.gentlemanspa.ui.customerDashboard.fragment.chat.chat.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.app.gentlemanspa.databinding.ItemMyMessageBinding
import com.app.gentlemanspa.ui.customerDashboard.fragment.chat.chat.model.CustomerChatModel
import com.app.gentlemanspa.utils.AppPrefs
import com.app.gentlemanspa.utils.CUSTOMER_USER_ID
import com.app.gentlemanspa.utils.PROFESSIONAL_USER_ID
import com.app.gentlemanspa.utils.ROLE
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CustomerChatAdapter (val context: Context, private val UserMessageList: MutableList<CustomerChatModel?>) : RecyclerView.Adapter<CustomerChatAdapter.MessageViewHolder>(){
    private var userRef: DatabaseReference? = null
    var onItemClickListener : OnItemClickListener? =null

    interface OnItemClickListener{
        fun deleteForEveryone(position: Int, userMessageList: MutableList<CustomerChatModel?>)
    }

    fun setOnMyItemClickListener(mItemClickListener: OnItemClickListener){
        this.onItemClickListener =mItemClickListener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder(ItemMyMessageBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val messageSenderId = "${AppPrefs(context).getStringPref(CUSTOMER_USER_ID)}"


        val messages = UserMessageList[position]
        val fromUserId = messages?.from
        val frommessagetype = messages?.type
        userRef = FirebaseDatabase.getInstance().reference.child("Users").child(fromUserId!!)
        userRef!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.hasChild("image")) {
                    /*      val receiverprofileimage = dataSnapshot.child("image").value.toString()
                        Glide.with(holder.itemView.context.applicationContext).load(receiverprofileimage)
                             //.placeholder(R.drawable)
                             .into(holder.receiverprofileimage)*/
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        holder.receivermessagetext.visibility = View.GONE
        holder.timeReceiver.visibility = View.GONE
        holder.timeSender.visibility = View.GONE
        //   holder.receiverprofileimage.visibility = View.GONE
        holder.sendermessagetext.visibility = View.GONE
        holder.messageSenderPicture.visibility = View.GONE
        holder.messageReceiverPicture.visibility = View.GONE
        Log.d("ChatAdapterLog", "onBindViewHolderFromMessageType: $frommessagetype    $fromUserId    $messageSenderId")
        if (frommessagetype == "text") {
            if (fromUserId == messageSenderId) {
                holder.sendermessagetext.visibility = View.VISIBLE
                holder.timeSender.visibility = View.VISIBLE
//              holder.sendermessagetext.setBackgroundResource(R.drawable.sender_message_layout)
                holder.sendermessagetext.text = "${messages?.message}"

                Log.d("ChatAdapterLog", "onBindViewHolderTime: ${messages.time}")
                holder.timeSender.text ="${messages.time}"
            } else {
                holder.receivermessagetext.visibility = View.VISIBLE
                holder.timeReceiver.visibility = View.VISIBLE
                //   holder.receiverprofileimage.visibility = View.GONE
//                holder.receivermessagetext.setBackgroundResource(R.drawable.receiver_message_layout)
                holder.receivermessagetext.text = "${messages.message}"

                Log.d(TAG, "onBindViewHolderTimeRECEIVER: ${messages.time}")
                holder.timeReceiver.text ="${messages.time}"
            }
        } else if (frommessagetype == "image") {
            if (fromUserId == messageSenderId) {
                holder.messageSenderPicture.visibility = View.VISIBLE
                Glide.with(holder.itemView.context).load(messages.message).into(holder.messageSenderPicture)
            } else {
                holder.messageReceiverPicture.visibility = View.VISIBLE
                //        holder.receiverprofileimage.visibility = View.GONE
                Glide.with(holder.itemView.context).load(messages.message).into(holder.messageReceiverPicture)
            }
        } else if (frommessagetype == "pdf" || frommessagetype == "docx") {
            if (fromUserId == messageSenderId) {
                holder.messageSenderPicture.visibility = View.VISIBLE
                Glide.with(holder.itemView.context).load(messages.message).into(holder.messageSenderPicture)
            } else {
                holder.messageReceiverPicture.visibility = View.VISIBLE
                Glide.with(holder.itemView.context).load(messages.message).into(holder.messageReceiverPicture)
                //          holder.receiverprofileimage.visibility = View.GONE
            }
        }
        if (fromUserId == messageSenderId) {
            holder.messageSenderCV.setOnClickListener {
                if (UserMessageList[position]?.type == "pdf" || UserMessageList[position]?.type == "docx") {
                    val options = arrayOf<CharSequence>(
                        "Delete for me",
                        "Download and view content",
                        "Cancel",
                        "Delete for everyone"
                    )
                    val builder = AlertDialog.Builder(holder.itemView.context)
                    builder.setTitle("Delete Message?")
                    builder.setItems(options) { dialog, which ->
                        if (which == 0) {
                            deleteSentMessage(position, holder)
                        } else if (which == 1) {
                            val intent = Intent(
                                Intent.ACTION_VIEW, Uri.parse(
                                    UserMessageList[position]?.message
                                )
                            )
                            holder.itemView.context.startActivity(intent)
                        } else if (which == 2) {
                            //for cancel do not do anything
                        } else if (which == 3) {
                            onItemClickListener?.deleteForEveryone(position,UserMessageList)
                            //  deleteMessageForEveryone(position, holder)
                        }
                    }
                    builder.show()
                } else if (UserMessageList[position]?.type == "text") {
                    val options = arrayOf<CharSequence>(
                        "Delete for me", "Delete for everyone", "Cancel"
                    )
                    val builder = AlertDialog.Builder(holder.itemView.context)
                    builder.setTitle("Delete Message?")
                    builder.setItems(options) { dialog, which ->
                        if (which == 0) {
                            deleteSentMessage(position, holder)
                        } else if (which == 1) {
                            onItemClickListener?.deleteForEveryone(position,UserMessageList)
                            // deleteMessageForEveryone(position, holder)
                        } else if (which == 2) {
                            //for cancel do not do anything
                        }
                    }
                    builder.show()
                } else if (UserMessageList[position]?.type == "image") {
                    val options = arrayOf<CharSequence>(
                        "Delete for me",
                        // "View This Image",
                        "Cancel",
                        "Delete for everyone"
                    )
                    val builder = AlertDialog.Builder(holder.itemView.context)
                    builder.setTitle("Delete Message?")
                    builder.setItems(options) { dialog, which ->
                        if (which == 0) {
                            deleteSentMessage(position, holder)
                        }
//                        else if (which == 1) {
//                            val intent =
//                                Intent(holder.itemView.context, ImageViewActivity::class.java)
//                            intent.putExtra("url", UserMessageList[position]?.message)
//                            holder.itemView.context.startActivity(intent)
//                        }
                        else if (which == 1) {
                            //for cancel do not do anything
                        } else if (which == 2) {
                            onItemClickListener?.deleteForEveryone(position,UserMessageList)
                            // deleteMessageForEveryone(position, holder)
                        }
                    }
                    builder.show()
                }
            }
        } else {
            holder.messageReceiverCV.setOnClickListener {
                if (UserMessageList[position]?.type == "pdf" || UserMessageList[position]?.type == "docx") {
                    val options = arrayOf<CharSequence>(
                        "Delete for me", "Download and view content", "Cancel"
                    )
                    val builder = AlertDialog.Builder(holder.itemView.context)
                    builder.setTitle("Delete Message?")
                    builder.setItems(options) { dialog, which ->
                        if (which == 0) {
                            deleteReceiveMessage(position, holder)
                        } else if (which == 1) {
                            val intent = Intent(
                                Intent.ACTION_VIEW, Uri.parse(
                                    UserMessageList[position]?.message
                                )
                            )
                            holder.itemView.context.startActivity(intent)
                        } else if (which == 2) {
                            //for cancel do not do anything
                        }
                    }
                    builder.show()
                } else if (UserMessageList[position]?.type == "text") {
                    val options = arrayOf<CharSequence>(
                        "Delete for me", "Cancel"
                    )
                    val builder = AlertDialog.Builder(holder.itemView.context)
                    builder.setTitle("Delete Message?")
                    builder.setItems(options) { dialog, which ->
                        if (which == 0) {
                            deleteReceiveMessage(position, holder)
                        } else if (which == 1) {
                            //for cancel do not do anything
                        }
                    }
                    builder.show()
                }
                else if (UserMessageList[position]?.type == "image") {
                    val options = arrayOf<CharSequence>(
                        "Delete for me", "Cancel"
                    )
                    val builder = AlertDialog.Builder(holder.itemView.context)
                    builder.setTitle("Delete Message?")
                    builder.setItems(options) { dialog, which ->
                        if (which == 0) {
                            deleteReceiveMessage(position, holder)
                        } else if (which == 2) {
//                            val intent =
//                                Intent(holder.itemView.context, ImageViewActivity::class.java)
//                            intent.putExtra("url", UserMessageList[position]?.message)
//                            holder.itemView.context.startActivity(intent)
                        } else if (which == 1) {
                            //for cancel do not do anything
                        }
                    }
                    builder.show()
                }
            }
        }
    }

    private fun deleteSentMessage(position: Int, holder: MessageViewHolder) {
        val rootRef = FirebaseDatabase.getInstance().reference
        rootRef.child("Messages").child(UserMessageList[position]?.from!!)
            .child(UserMessageList[position]?.to!!)
            .child(UserMessageList[position]?.messageID!!)
            .removeValue().addOnCompleteListener { task ->
                if (task.isSuccessful) {

//                    notifyItemRemoved(position)
//                    UserMessageList.removeAt(position)
//                    notifyItemRangeChanged(position, UserMessageList.size)
                    Toast.makeText(
                        holder.itemView.context,
                        "Message deleted...",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(holder.itemView.context, "Error...", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun deleteReceiveMessage(position: Int, holder: MessageViewHolder) {
        val rootRef = FirebaseDatabase.getInstance().reference
        rootRef.child("Messages").child(UserMessageList[position]?.to!!)
            .child(UserMessageList[position]?.from!!)
            .child(UserMessageList[position]?.messageID!!)
            .removeValue().addOnCompleteListener { task ->
                if (task.isSuccessful) {
//                    notifyItemRemoved(position)
//                    UserMessageList.removeAt(position)
//                    notifyItemRangeChanged(position, UserMessageList.size)
                    Toast.makeText(
                        holder.itemView.context,
                        "Message deleted...",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(holder.itemView.context, "Error...", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun deleteMessageForEveryone(position: Int, holder: MessageViewHolder) {
        val rootRef = FirebaseDatabase.getInstance().reference
        rootRef.child("Messages").child(UserMessageList[position]?.from!!)
            .child(UserMessageList[position]?.to!!)
            .child(UserMessageList[position]?.messageID!!)
            .removeValue().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val rootRef = FirebaseDatabase.getInstance().reference
                    rootRef.child("Messages").child(UserMessageList[position]?.to!!)
                        .child(UserMessageList[position]?.from!!)
                        .child(UserMessageList[position]?.messageID!!)
                        .removeValue().addOnCompleteListener { task1 ->
                            if (task1.isSuccessful) {
                                notifyItemRemoved(position)
                                UserMessageList.removeAt(position)
                                notifyItemRangeChanged(position, UserMessageList.size)

                                Toast.makeText(
                                    holder.itemView.context,
                                    "Message deleted...",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    holder.itemView.context,
                                    "Error...",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(holder.itemView.context, "Error...", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun getItemCount(): Int {
        return UserMessageList.size
    }

    class MessageViewHolder(itemView: ItemMyMessageBinding) : RecyclerView.ViewHolder(itemView.root) {
        val binding : ItemMyMessageBinding =itemView
        var sendermessagetext: TextView
        var receivermessagetext: TextView
        var timeSender : TextView
        var timeReceiver : TextView
        //   var receiverprofileimage: CircleImageView
        var messageSenderPicture: ImageView
        var messageReceiverPicture: ImageView
        var messageReceiverCV: CardView
        var messageSenderCV: CardView

        init {
            sendermessagetext = binding.msgSenderTV
            receivermessagetext = binding.msgReceiverTV
            timeSender =binding.msgTimeSenderTV
            timeReceiver =binding.msgTimeReceiverTV
            // receiverprofileimage = binding.messageProfileImage
            messageSenderPicture = binding.messageSenderImageView
            messageReceiverPicture = binding.messageReceiverImageView
            messageReceiverCV = binding.msgReceiverCV
            messageSenderCV =binding.msgSenderCV

        }
    }
}
