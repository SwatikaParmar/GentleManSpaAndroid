package com.app.gentlemanspa.ui.professionalDashboard.fragment.chat.messages

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.gentlemanspa.databinding.DialogRemoveUserChatBinding
import com.app.gentlemanspa.databinding.FragmentProfessionalMessagesBinding
import com.app.gentlemanspa.databinding.ItemMessagesBinding
import com.app.gentlemanspa.network.ApiConstants
import com.app.gentlemanspa.ui.professionalDashboard.fragment.chat.messages.model.ProfessionalMessageModel
import com.app.gentlemanspa.utils.CommonUtils
import com.app.gentlemanspa.utils.showToast
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


@Suppress("NAME_SHADOWING")
class ProfessionalMessageFragment : Fragment(), View.OnClickListener {
  lateinit var binding:FragmentProfessionalMessagesBinding
    private var currentUserId: String? = null
    private var contactsRef: DatabaseReference? = null
    private  var userRef: DatabaseReference? = null
    private val args: ProfessionalMessageFragmentArgs by navArgs()
    var dialogBuilder: AlertDialog.Builder? = null
    var myDialog: AlertDialog? = null
    var adapter : FirebaseRecyclerAdapter<ProfessionalMessageModel?, ContactsViewHolder?>? = null
    var onlineStatusBlockReceiver :Any? =null
    var onlineStatusBlockSender :Any? =null
    var image=""
    var name=""
    var genderIs:Any? =null
    var uid=""
    var status=""
    companion object {
        var fromMessageNotificationUserId =""
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!this::binding.isInitialized){
            binding=FragmentProfessionalMessagesBinding.inflate(layoutInflater,container,false)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // currentUserId = "${AppPrefs(requireContext()).getStringPref(USER_ID)}"
        currentUserId = args.userId
        Log.d(TAG, "onCreateView currentUseridChat: $currentUserId")
        contactsRef = FirebaseDatabase.getInstance().reference.child("Contacts").child(currentUserId!!)
        userRef = FirebaseDatabase.getInstance().reference.child("Users")
        val mLinearLayoutManager = LinearLayoutManager(context)
        mLinearLayoutManager.reverseLayout =true
        binding.rvMessages.layoutManager = mLinearLayoutManager
        initUI()
    }

    private fun initUI() {
        binding.onClick=this

    }
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResumeChatMessage: called")
        showNoDataFound()
    }
    override fun onStart() {
        super.onStart()
        userMessages()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun userMessages() {
        //FOR LATEST MESSAGE ON TOP-----
        val options= contactsRef?.orderByChild("latest_message/timeStamp").let {
            FirebaseRecyclerOptions.Builder<ProfessionalMessageModel>()
                .setQuery(it!!, ProfessionalMessageModel::class.java)
                .build()
        }

        adapter = object : FirebaseRecyclerAdapter<ProfessionalMessageModel?, ContactsViewHolder?>(options){
            override fun onBindViewHolder(
                holder: ContactsViewHolder,
                @SuppressLint("RecyclerView") position: Int,
                model: ProfessionalMessageModel
            ) {
                val userId = getRef(position).key.toString()
                Log.d("TAG", "onDataChangeUSERiD: $userId  $position")
                userRef?.child(userId)?.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            FirebaseDatabase.getInstance().reference.child("Contacts").child(currentUserId.toString()).child(userId)
                                .child("ContactStatus").child("BlockStatus").addListenerForSingleValueEvent(object :
                                    ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if(snapshot.exists()){
                                            showNoDataFound()
                                              binding.noDataFoundMessage.visibility =View.GONE
                                        }else{
                                            showNoDataFound()
                                             binding.noDataFoundMessage.visibility =View.VISIBLE
                                        }
                                        onlineStatusBlockReceiver =snapshot.value
                                    }
                                    override fun onCancelled(error: DatabaseError) {}})

                            FirebaseDatabase.getInstance().reference.child("Contacts").child(userId).child(currentUserId.toString())
                                .child("ContactStatus").child("BlockStatus")
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        onlineStatusBlockSender =snapshot.value
                                    }
                                    override fun onCancelled(error: DatabaseError) {} })
                            Log.d(TAG, "onDataChangeBlockStatusOnline: $onlineStatusBlockReceiver  $onlineStatusBlockSender")
                            if(onlineStatusBlockReceiver==true &&  onlineStatusBlockSender==true){
                                holder.onlineIcon.visibility = View.GONE
                            }else if(onlineStatusBlockReceiver==true){
                                holder.onlineIcon.visibility = View.GONE
                            }else if(onlineStatusBlockSender==true){
                                holder.onlineIcon.visibility = View.GONE
                            } else{
                                if (dataSnapshot.child("userState").hasChild("state")) {
                                    val state = dataSnapshot.child("userState")
                                        .child("state").value.toString()
                                 /*   val date = dataSnapshot.child("userState")
                                        .child("date").value.toString()
                                    val time = dataSnapshot.child("userState")
                                        .child("time").value.toString()*/
                                    if (state == "online") {
                                        holder.onlineIcon.visibility = View.VISIBLE
                                    } else if (state == "offline") {
                                        holder.onlineIcon.visibility = View.GONE
                                    }
                                }  else {
                                    holder.onlineIcon.visibility = View.GONE
                                }
                            }

                            if (dataSnapshot.hasChild("image")) {
                                image = dataSnapshot.child("image").value.toString()
                                name = dataSnapshot.child("name").value.toString()
                                val genderValueIs = dataSnapshot.child("gender").value
                                Log.d(TAG, "onDataChangeGenderIs: $genderIs")
                                if(genderValueIs != null){
                                    genderIs =genderValueIs.toString()
                                }else{
                                    genderIs =0
                                }
                                //   uid = dataSnapshot.child("uid").value.toString()
                                status = dataSnapshot.child("status").value.toString()
                                holder.username.text = CommonUtils.capitaliseOnlyFirstLetter(name)
                                holder.userStatus.text = status
                                context?.let {
                                 //   if(image != "" && image != null){
                                    if(image != "" && image != "null"){
                                        Log.d("TAG", "onDataChangeGender: ${ApiConstants.BASE_FILE}$image  gender $genderIs")
                                         Glide.with(it).load(ApiConstants.BASE_FILE+image).placeholder(com.app.gentlemanspa.R.drawable.profile_placeholder).into(holder.profilePicture)
                                    }else{
                                        Log.d("TAG", "onDataChangeGenderElse: ${ApiConstants.BASE_FILE}$image  gender $genderIs")
                                        holder.profilePicture.setImageResource(com.app.gentlemanspa.R.drawable.profile_placeholder)

                                    }
                                }

                            } else {
                                val name = dataSnapshot.child("name").value.toString()
                                val status = dataSnapshot.child("status").value.toString()
                                holder.username.text = name
                                holder.userStatus.text = status
                            }

                            contactsRef?.child(userId)?.addValueEventListener(object :
                                ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if(snapshot.exists()){
                                        if(snapshot.child("latest_message").hasChild("message")){
                                            val lastMessage =snapshot.child("latest_message").child("message").value.toString()
                                            val messageTime =snapshot.child("latest_message").child("time").value.toString()

                                            Log.d("UserMessage","lastMessage->$lastMessage messageTime->$messageTime")
                                            holder.binding.tvUserMessage.text =lastMessage
                                            holder.binding.tvMessageTime.text =messageTime
                                        }
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {

                                }

                            })

                            holder.itemView.setOnClickListener {
                                uid =getRef(position).key.toString()
                                val action =  ProfessionalMessageFragmentDirections.actionProfessionalMessageFragmentToProfessionalChatFragment(
                                    arguments?.getString("userId").toString(),
                                    uid,
                                    "FromMessages")
                               findNavController().navigate(action)

                            }


                            holder.itemView.setOnLongClickListener {
                                dialogBuilder = AlertDialog.Builder(context)
                                val dialogRemoveUserChatBinding: DialogRemoveUserChatBinding =
                                    DialogRemoveUserChatBinding.inflate(
                                        LayoutInflater.from(
                                            requireContext()
                                        )
                                    )
                                dialogBuilder!!.setView(dialogRemoveUserChatBinding.root)
                                dialogRemoveUserChatBinding.tvCancel.setOnClickListener {
                                    myDialog?.dismiss()
                                }
                                dialogRemoveUserChatBinding.tvDelete.setOnClickListener {
                                    myDialog?.dismiss()
                                    val removeUserUserId = getRef(position).key.toString()
                                    Log.d(TAG, "onLongClickDeleteUser: $removeUserUserId")
                                    FirebaseDatabase.getInstance().reference.child("Messages")
                                        .child(currentUserId!!).child(removeUserUserId)
                                        .removeValue().addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                contactsRef?.child(removeUserUserId)?.removeValue()
                                                    ?.addOnCompleteListener { task ->
                                                        if (task.isSuccessful) {
                                                            showNoDataFound()
                                                            adapter?.notifyDataSetChanged()
                                                            requireContext().showToast("User Deleted Successfully!")
                                                        } else {
                                                            Toast.makeText(
                                                                context,
                                                                "Something Went Wrong While Deleting User!",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        }

                                                    }
                                            }
                                        }
                                }

                                myDialog = dialogBuilder?.create()
                                if (myDialog != null && myDialog?.window != null) {
                                    myDialog?.window?.setBackgroundDrawable(
                                        ColorDrawable(Color.TRANSPARENT)
                                    )
                                }
                                myDialog?.show()
                                myDialog?.setCancelable(true)
                                true
                            }

                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })

            }


            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
                return ContactsViewHolder(
                    ItemMessagesBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false)
                )
            }
        }

        binding.rvMessages.adapter = adapter
        adapter?.startListening()
        adapter?.notifyDataSetChanged()
        Log.d(TAG, "onStartMyAdapterCount: ${adapter?.itemCount}")

    }

    fun showNoDataFound(){
        contactsRef?.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d(TAG, "onViewCreatedMessageAdapterCountAdapter: ${snapshot.childrenCount}  currentUserId -${currentUserId}")
                if(snapshot.childrenCount >0){
                    binding.noDataFoundMessage.visibility =View.GONE
                }else{
                    binding.noDataFoundMessage.visibility =View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Something Went Wrong!", Toast.LENGTH_SHORT).show()
            }
        })
    }

class ContactsViewHolder(itemView: ItemMessagesBinding) :
    RecyclerView.ViewHolder(itemView.root) {
    val binding :ItemMessagesBinding =itemView
    var username: TextView = binding.tvName
    var userStatus: TextView = binding.tvMessageTime
    var profilePicture: ShapeableImageView = binding.ivProfile
    var onlineIcon: ImageView = binding.usersOnlineStatus
}
    override fun onClick(v: View?) {
        when(v){
            binding.ivArrowBack->{
                findNavController().popBackStack()
            }
        }
    }




}