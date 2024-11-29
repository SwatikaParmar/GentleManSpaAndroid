package com.app.gentlemanspa.ui.customerDashboard.fragment.messages.adapter

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.gentlemanspa.R
import com.app.gentlemanspa.databinding.ItemMessagesBinding
import com.app.gentlemanspa.network.ApiConstants
import com.app.gentlemanspa.ui.customerDashboard.fragment.messages.model.MessageModel
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase

class MessagesAdapter(userId :String) : ListAdapter<MessageModel, MessagesAdapter.MyViewHolder>(COMPARATOR) {
        var onItemClickListener : onItemClickInterface? =null
        val contactsRef = FirebaseDatabase.getInstance().reference.child("Contacts").child(userId)
        var mItem :ArrayList<MessageModel> = ArrayList()

        interface onItemClickInterface : AdapterView.OnItemClickListener{
            fun onMyItemClick(item: MessageModel?, position: Int)

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            return MyViewHolder(ItemMessagesBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        }

        fun setOnMyItemClickListener(mItemClickListener: onItemClickInterface){
            this.onItemClickListener =mItemClickListener
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val item =getItem(position)
            Log.d(ContentValues.TAG, "onBindViewHolder: ${mItem.size}")
            holder.binding.tvName.text =item.name
        }

        class MyViewHolder(itemView : ItemMessagesBinding):RecyclerView.ViewHolder(itemView.root) {
            var binding : ItemMessagesBinding =itemView
        }
        override fun getItemViewType(position: Int): Int = position

        companion object{
            var COMPARATOR =object : DiffUtil.ItemCallback<MessageModel>(){
                override fun areItemsTheSame(
                    oldItem: MessageModel,
                    newItem: MessageModel
                ): Boolean {
                    return  oldItem==newItem
                }

                override fun areContentsTheSame(
                    oldItem: MessageModel,
                    newItem: MessageModel
                ): Boolean {
                    return oldItem.name == newItem.name
                }

            }
        }
/*    private lateinit var messagesCallbacks:MessagesCallbacks

    class ViewHolder(val binding : ItemMessagesBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMessagesBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return 10
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            tvName.text="Kim"
            tvMessage.text="hello"
            tvTime.text="34 mins"
            Glide.with(holder.itemView.context).load(ApiConstants.BASE_FILE +"0").error(
                R.drawable.profile_placeholder).into(ivProfile)
            root.setOnClickListener {
                messagesCallbacks.onMessageItemClick()
            }
        }
    }
   fun setMessagesCallbacks(onClick:MessagesCallbacks){
       messagesCallbacks=onClick
   }

   interface MessagesCallbacks{
       fun onMessageItemClick()
   }*/

}