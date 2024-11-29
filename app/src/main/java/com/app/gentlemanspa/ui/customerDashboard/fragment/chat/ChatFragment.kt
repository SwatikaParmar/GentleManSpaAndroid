package com.app.gentlemanspa.ui.customerDashboard.fragment.chat

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Lifecycle
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.gentlemanspa.base.MyApplication
import com.app.gentlemanspa.base.MyApplication.Companion
import com.app.gentlemanspa.databinding.FragmentChatBinding
import com.app.gentlemanspa.network.ApiConstants
import com.app.gentlemanspa.ui.customerDashboard.fragment.chat.adapter.ChatAdapter
import com.app.gentlemanspa.ui.customerDashboard.fragment.messages.MessageFragment
import com.app.gentlemanspa.utils.AppPrefs
import com.app.gentlemanspa.utils.CommonUtils
import com.app.gentlemanspa.utils.PROFILE_CUSTOMER_DATA
import com.app.gentlemanspa.utils.USER_ID
import com.app.gentlemanspa.utils.getCurrentTime
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.R
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.trubbled.ui.main.chat.model.MyChatModel
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.HashMap
import java.util.Locale


class ChatFragment : Fragment(), View.OnClickListener {
    lateinit var binding: FragmentChatBinding
    var dialogBuilder: AlertDialog.Builder? = null
    var alertDialog1: AlertDialog? = null
    var alertDialog: AlertDialog? = null

    private var response: Response? = null
    var usertoken: String? = null
    var myTxtMsg: String = ""

    private val argsFromUserProfile: ChatFragmentArgs by navArgs()
    var firebaseDataBase: FirebaseDatabase? = null
    var myBlockStatus: Any? = null
    var dialog: BottomSheetDialog? = null

    //var alertUnBlockDialogBinding :UnblockAlertDialogBinding? =null
    var alertDialog2: AlertDialog? = null

    //firebase database ref
    private var contactsRef: DatabaseReference? = null

    // var databaseReference

    var visible = ""
    var problemDesc: String = ""
    var reportId = 0
    var reportType = ""
    var postId = 0
    var nameIs = ""
    var imageIs = ""
    var genderIs: Int? = null
    var myIndex: Int? = null

    var currentUserId = ""
    var userRef: DatabaseReference? = null
    private var chatAdapter: ChatAdapter? = null
    private var messageSenderId: String? = null
    private var messageRecieverId: String? = null
    private var RootRef: DatabaseReference? = null
    private var loadingBar: ProgressDialog? = null
    private var savecurrentTime: String? = null
    private var savecurrentDate: String? = null
    private var userlastseen: TextView? = null
    private var fileuri: Uri? = null
    private var checker = ""
    private var textDeleteSpace = ""
    private var linearLayoutManager: LinearLayoutManager? = null
    private var messagesList: MutableList<MyChatModel?> = ArrayList()
    private val FCM_API = "https://fcm.googleapis.com/fcm/send"
    private val serverKey =
        "AAAA9DE-xZQ:APA91bF9MXwYOv6UIqaW4GUyVtPic16dUKOTKt-H4SQ8gm_zGG2RP8F88m2BUvsMWOO0b--FtCK1D07iAr0HWPi1QfBDePQhU_P6Ou4Z32tz2OjQ0QtCAhFP1yeE8Q3rb8Q3IyBF5wam"
    private val contentType = "application/json"
    val TAG = "NOTIFICATION TAG"


    var NOTIFICATION_TITLE: String? = null
    var NOTIFICATION_MESSAGE: String? = null
    var TOPIC: String? = null
    // private val apiService: APIService? = null

    companion object {
        var sender = ""
    }

    /* private lateinit var viewModelChat: ChatViewModel
     override fun getRootView(): View? {
         return binding.root
     }*/


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentUserId = AppPrefs(MyApplication.context).getString("TOKEN").toString()
        contactsRef = FirebaseDatabase.getInstance().reference.child("Contacts")

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

    private fun initUI() {
        binding.onClick = this
        setUpChat()
    }

    private fun setUpChat() {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        messageSenderId = "${AppPrefs(requireContext()).getStringPref(USER_ID)}"
        userRef = FirebaseDatabase.getInstance().reference.child("Users")
        RootRef = FirebaseDatabase.getInstance().reference
        chatAdapter = ChatAdapter(requireContext(), messagesList)
        Log.d(TAG, "oncalled onViewCreatedChatCreated")
        //for emoji support in chat
        // EmojiManager.install(GoogleEmojiProvider())

//        binding.chatMsgET.setOnFocusChangeListener { view, b ->
//            if(b){
//                Log.d(TAG, "onViewCreatedOmChatClick: $b  ${chatAdapter?.itemCount}")
//                chatAdapter?.itemCount?.let { binding.recycleMyMessage.scrollToPosition(it-1) }
//            }
//        }

        val uId = MessageFragment.fromMessageNotificationUserId
        Log.d(TAG, "onViewCreatedNotificationuid: ${uId}")
        if (!uId.isNullOrEmpty()) {
            messageRecieverId = uId
            MessageFragment.fromMessageNotificationUserId = ""
        } else {
            val dataFromArgs = ChatFragmentArgs.fromBundle(requireArguments())
            if (dataFromArgs.from.equals("FromUserProfile")) {
                messageRecieverId = dataFromArgs.userId
            } else {
                messageRecieverId = dataFromArgs.userId
            }
            Log.d(
                TAG,
                "onViewCreatedByNavigationArgsUserId: ${messageRecieverId}   from${dataFromArgs.from}"
            )
        }

        binding.tvName.text = nameIs
        linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager?.stackFromEnd = true
//        linearLayoutManager?.reverseLayout =false
//        binding.recycleMyMessage.setNestedScrollingEnabled(true)
        binding.recycleMyMessage.layoutManager = linearLayoutManager
        RootRef!!.child("Messages").child(messageSenderId!!).child(messageRecieverId!!)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val countIs = snapshot.childrenCount.toInt()
                        Log.d(TAG, "onDataChangeSendMessageChildCount: ${countIs}")
//                    binding.recycleMyMessage.smoothScrollToPosition(countIs-1)
                        linearLayoutManager?.scrollToPosition(countIs)
                        chatAdapter?.notifyDataSetChanged()
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })


        binding.recycleMyMessage.adapter = chatAdapter
        chatAdapter?.setOnMyItemClickListener(object : ChatAdapter.OnItemClickListener {
            override fun deleteForEveryone(
                position: Int, userMessageList: MutableList<MyChatModel?>
            ) {
                val messageId = userMessageList[position]?.messageID
                RootRef!!.child("Messages").child(messageSenderId.toString())
                    .child(messageRecieverId.toString()).child(messageId.toString()).removeValue()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            RootRef!!.child("Messages").child(messageRecieverId.toString())
                                .child(messageSenderId.toString()).child(messageId.toString())
                                .removeValue().addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Log.d(TAG, "deleteForEveryone: Success")
                                    }
                                }
                        }
                    }
                Log.d(TAG, "deleteMessageForEveryoneChatInterface: Called")
            }
        })

        RootRef?.child("Messages")?.child(messageSenderId!!)?.child(messageRecieverId.toString())
            ?.addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {}
                override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}
                @SuppressLint("NotifyDataSetChanged")
                override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                    Log.d(
                        TAG,
                        "onChildRemovedObserver: value -${dataSnapshot.value}   Key-${dataSnapshot.key}"
                    )
                    myIndex = -1
                    for (i in 0 until messagesList.size) {
                        if (messagesList.get(i)?.messageID == dataSnapshot.key) {
                            myIndex = i
                        }
                    }
                    Log.d(TAG, "onChildRemovedMyIndex: ${myIndex}")
                    if (myIndex == -1) {

                    } else {
                        if (messagesList.size > myIndex!!) {
                            messagesList.removeAt(myIndex!!)
                            chatAdapter?.notifyDataSetChanged()
                        }
                    }
//                    messagesList.clear()
//                    val messages = dataSnapshot.getValue(
//                        MyChatModel::class.java)
//                    messagesList.add(messages)
//                    chatAdapter!!.notifyDataSetChanged()
//                    binding.recycleMyMessage.smoothScrollToPosition(
//                        binding.recycleMyMessage.getAdapter()!!.itemCount
//                    )

                }
                override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
                override fun onCancelled(databaseError: DatabaseError) {}
            })
        Log.d(TAG, "onViewCreatedChatScreenUserId: $messageRecieverId")
        addChildToList()
        sender = messageRecieverId.toString()
        loadingBar = ProgressDialog(requireContext())
        userlastseen = binding.tvLastSeen
        binding.recycleMyMessage.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
                visible = "Visible"
                binding.reportCV.visibility = View.GONE
            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

            }
        })


//CURRENT DATE TIME
        val calendar = Calendar.getInstance()
        val currentDate = SimpleDateFormat("dd/MM/yyyy")
        savecurrentDate = currentDate.format(calendar.time)
        val currentTime = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val timeampm = currentTime.format(calendar.time)
        Log.d(TAG, "onViewCreatedTimeIs: $timeampm")
        val timeAMPM = timeampm.replace("am", "AM").replace("pm", "PM");
        Log.d(TAG, "onViewCreatedTimeIsAMPM: $timeAMPM")
        savecurrentTime = timeAMPM
        displayLastSeen()



//FOR BLOCK USER IN CHAT MESSAGE SCREEN
        RootRef?.child("Contacts")?.child(messageSenderId.toString())
            ?.child(messageRecieverId.toString())?.child("ContactStatus")?.child("BlockStatus")
            ?.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    myBlockStatus = snapshot.value

                    RootRef?.child("Contacts")?.child(messageRecieverId.toString())
                        ?.child(messageSenderId.toString())?.child("ContactStatus")
                        ?.child("BlockStatus")?.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                Log.d(
                                    TAG,
                                    "onDataChangeBlockStatus: ${myBlockStatus}  ${snapshot.value}"
                                )
                                if (myBlockStatus == true && snapshot.value == true) {
                                    binding.blockBottomCL.visibility = View.VISIBLE
                                    binding.typeChatCL.visibility = View.GONE
                                    binding.settingRL.visibility = View.GONE
                                    binding.blockBottomTextView.visibility = View.GONE
                                    binding.rootCLChat.setBackgroundColor(Color.parseColor("#E7E5E2"))
                                } else {
                                    if (myBlockStatus == true) {
                                        binding.blockBottomCL.visibility = View.VISIBLE
                                        binding.typeChatCL.visibility = View.GONE
                                        binding.settingRL.visibility = View.GONE
                                        binding.rootCLChat.setBackgroundColor(Color.parseColor("#E7E5E2"))
                                    } else if (snapshot.value == true) {
                                        binding.typeChatCL.visibility = View.GONE
                                        binding.blockBottomCL.visibility = View.GONE
                                        binding.blockBottomTextView.visibility = View.VISIBLE
                                        binding.rootCLChat.setBackgroundColor(Color.parseColor("#E7E5E2"))
                                    } else {
                                        binding.typeChatCL.visibility = View.VISIBLE
                                        binding.blockBottomCL.visibility = View.GONE
                                        binding.blockBottomTextView.visibility = View.GONE
                                        binding.settingRL.visibility = View.VISIBLE
                                        binding.rootCLChat.setBackgroundColor(Color.parseColor("#333333"))
                                    }
                                }

                            }

                            override fun onCancelled(error: DatabaseError) {
                            }
                        })
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })



        binding.chatMsgET.doOnTextChanged { text, start, before, count ->
            if (!text.isNullOrEmpty()) {
                Log.d(TAG, "onViewCreatedOnTextChange: true")
//               textDeleteSpace =text.toString()
//                binding.chatMsgET.inputType =InputType.TYPE_TEXT_FLAG_MULTI_LINE
                binding.chatMsgET.setSingleLine(false)
                binding.chatMsgET.maxLines = 4
                binding.chatMsgET.minLines = 1
                binding.chatMsgET.setSelection(text.toString().length)
            } else {
                binding.chatMsgET.setSingleLine(true)
                Log.d(TAG, "onViewCreatedOnTextChange: Else")
            }
        }

        binding.sendMsgCV.setOnClickListener {
            binding.sendMsgCV.isEnabled = false
            textDeleteSpace = binding.chatMsgET.text.toString()
            if (textDeleteSpace?.startsWith(" ") == true) {
                Log.d(TAG, "onViewCreatedSendMsgCVTrue: ${textDeleteSpace}")
                myTxtMsg = textDeleteSpace.trimStart()
            } else {
                Log.d(TAG, "onViewCreatedSendMsgCVFalse: ${textDeleteSpace}")
                myTxtMsg = textDeleteSpace.toString()
            }
            sendMessage()
        }


        /*  binding.settingIV1.setOnClickListener {
              if (visible == "InVisible") {
                  visible = "Visible"
                  binding.reportCV.visibility = View.GONE

              } else {
                  visible = "InVisible"
                  binding.reportCV.visibility = View.VISIBLE
              }
          }

          binding.reportCL.setOnClickListener {
              binding.reportCV.visibility = View.GONE
              showReportDialog()
          }

          binding.blockCL.setOnClickListener {
              binding.reportCV.visibility = View.GONE
              showUserBlockDialog()
          }

          binding.unblockTV.setOnClickListener{
              showUnBlockDialog(messageRecieverId.toString())
          }

          val popUp =EmojiPopup.Builder.fromRootView(binding.root)
              .build(binding.chatMsgET)
          binding.smileIV.setOnClickListener {
              popUp.toggle()
          }*/
    }

    fun addChildToList() {
        Log.d(TAG, "onViewCreated: $messageSenderId $messageRecieverId")
        RootRef?.child("Messages")?.child(messageSenderId!!)?.child(messageRecieverId.toString())
            ?.addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                    Log.d(
                        TAG,
                        "onChildAddedObserver: value -${dataSnapshot.value}   Key-${dataSnapshot.key}  size-${dataSnapshot.childrenCount}"
                    )
                    val messages = dataSnapshot.getValue(MyChatModel::class.java)
                    messagesList.add(messages)
                    chatAdapter?.notifyDataSetChanged()
                    Log.d(
                        TAG,
                        "onViewCreatedAdapterPosition: adapter -${binding.recycleMyMessage.adapter!!.itemCount}  arrayCount-${messagesList.size}"
                    )
                    binding.recycleMyMessage.postDelayed({
//                     linearLayoutManager?.scrollToPosition(messagesList.size-1)
                        binding.recycleMyMessage.smoothScrollToPosition(binding.recycleMyMessage.adapter!!.itemCount)
                    }, 100)
                }

                override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}
                override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                }

                override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "oncalled  onResumeChatCreated")
        chatAdapter!!.notifyDataSetChanged()
        sender = messageRecieverId.toString()
        //  activity?.startService(Intent(activity,PushMessagingService::class.java))
    }

    /*    private fun showUnBlockDialog(messageReceiveMyUserId :String) {
            dialogBuilder = AlertDialog.Builder(context)
            alertUnBlockDialogBinding=
                UnblockAlertDialogBinding.inflate(LayoutInflater.from(context))
            dialogBuilder!!.setView(alertUnBlockDialogBinding?.root)


            alertUnBlockDialogBinding?.cancelTV?.setOnClickListener(View.OnClickListener {
                alertDialog2?.dismiss()
            })

            alertUnBlockDialogBinding?.unblockTV?.setOnClickListener {
                it.isEnabled =false
                viewModelChat.blockUserChat(messageReceiveMyUserId,false)
                alertDialog2?.dismiss()
            }

            alertDialog2 = dialogBuilder!!.create()

            if (alertDialog2 != null && alertDialog2!!.window != null) {
                alertDialog2!!.window?.setBackgroundDrawable( ColorDrawable(Color.TRANSPARENT));
            }
            alertDialog2!!.show()
            alertDialog2!!.setCancelable(true)
        }*/

    /*    fun showBottomSheetDialog(senderUserId: String, receiverUserId: String) {
            dialog = BottomSheetDialog(requireContext())
            val dialogBinding :BottomSheetUnblockChatBinding
                    = BottomSheetUnblockChatBinding.inflate(LayoutInflater.from(requireContext()))
            dialog?.setContentView(dialogBinding.root)
            dialog?.setCancelable(true)

            val btnUnBlock= dialogBinding.unblockTV


            btnUnBlock?.setOnClickListener {
                dialog?.dismiss()
                Toast.makeText(context, "UnBlock Pressed!!!", Toast.LENGTH_SHORT).show()
            }
    //        cancelBtn.setOnClickListener {
    //            dialog.dismiss()
    //        }
            dialog?.show()
        }*/

    /*   private fun showUserBlockDialog() {
           dialogBuilder = AlertDialog.Builder(context)
           val dialogProfileBlockBinding: DialogBlockUserFromChatBinding =
               DialogBlockUserFromChatBinding.inflate(LayoutInflater.from(context))
           dialogBuilder!!.setView(dialogProfileBlockBinding.root)


           dialogProfileBlockBinding.cancelTV.setOnClickListener(View.OnClickListener {
               alertDialog1?.dismiss()
           })

           dialogProfileBlockBinding.blockTV.setOnClickListener {
               viewModelChat.blockUserProfile(messageRecieverId,true)
               alertDialog1?.dismiss()
           }


           alertDialog1 = dialogBuilder!!.create()


           val displayMetrics = DisplayMetrics()
           if (alertDialog1 != null && alertDialog1!!.window != null) {
               alertDialog1!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
           }
   //        alertDialog1!!.window?.attributes?.windowAnimations =
   //            com.chaos.view.R.style.AlertDialog_AppCompat
   //        // alertDialog1.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics)
   //        val displayWidth: Int = displayMetrics.widthPixels
   //        val layoutParams: WindowManager.LayoutParams = WindowManager.LayoutParams()
   //        layoutParams.copyFrom(alertDialog1!!.window!!.attributes)
   //        // alertDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
   //        val dialogWindowWidth = (displayWidth * 1.05f).toInt()
   //        layoutParams.width = dialogWindowWidth
   //        layoutParams.gravity = Gravity.CENTER
   //        alertDialog1!!.window!!.attributes = layoutParams
           alertDialog1!!.show()
           alertDialog1!!.setCancelable(true)


       }*/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 555 && resultCode == AppCompatActivity.RESULT_OK && data != null && data.data != null) {
            loadingBar!!.setTitle("Sending File")
            loadingBar!!.setMessage("please wait, we are sending that file...")
            loadingBar!!.setCanceledOnTouchOutside(false)
            loadingBar!!.show()
            fileuri = data.data
            if (checker != "image") {
                val storageReference =
                    FirebaseStorage.getInstance().reference.child("Document Files")
                val messageSenderRef = "Messages/$messageSenderId/$messageRecieverId"
                val messageReceiverRef = "Messages/$messageRecieverId/$messageSenderId"
                val Usermessagekeyref = RootRef!!.child("Messages").child(messageSenderId!!).child(
                    messageRecieverId!!
                ).push()
                val messagePushID = Usermessagekeyref.key
                val filepath = storageReference.child("$messagePushID.$checker")
                filepath.putFile(fileuri!!).addOnSuccessListener {
                    filepath.downloadUrl.addOnSuccessListener { uri ->
                        val downloadUrl = uri.toString()
                        val messageDocsBody: MutableMap<String, Any> =
                            java.util.HashMap<String, Any>()

                        messageDocsBody["message"] = downloadUrl
                        messageDocsBody["name"] = fileuri!!.lastPathSegment.toString()
                        messageDocsBody["type"] = checker
                        messageDocsBody["from"] = messageSenderId.toString()
                        messageDocsBody["to"] = messageRecieverId.toString()
                        messageDocsBody["messageID"] ?: messagePushID
                        messageDocsBody["time"] = savecurrentTime.toString()
                        messageDocsBody["date"] = savecurrentDate.toString()
                        val messageBodyDDetail: MutableMap<String, Any> =
                            java.util.HashMap<String, Any>()
                        messageBodyDDetail["$messageSenderRef/$messagePushID"] = messageDocsBody
                        messageBodyDDetail["$messageReceiverRef/$messagePushID"] = messageDocsBody
                        RootRef!!.updateChildren(messageBodyDDetail)
                        loadingBar!!.dismiss()
                    }.addOnFailureListener { e ->
                        loadingBar!!.dismiss()
                        Toast.makeText(requireActivity(), e.message, Toast.LENGTH_SHORT).show()
                    }
                }.addOnProgressListener { taskSnapshot ->
                    val p = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                    loadingBar!!.setMessage(p.toInt().toString() + " % Uploading...")
                }
            } else if (checker == "image") {
                val storageReference = FirebaseStorage.getInstance().reference.child("Image Files")
                val messageSenderRef = "Messages/$messageSenderId/$messageRecieverId"
                val messageReceiverRef = "Messages/$messageRecieverId/$messageSenderId"
                val Usermessagekeyref = RootRef!!.child("Messages").child(messageSenderId!!).child(
                    messageRecieverId!!
                ).push()
                val messagePushID = Usermessagekeyref.key
                val filepath = storageReference.child("$messagePushID.jpg")
                // uploadTask = filepath.putFile(fileuri!!)
//                uploadTask.continueWithTask(object : Continuation<Any?, Any?> {
//                    @Throws(java.lang.Exception::class)
//                    fun then(task: Task<*>): Any? {
//                        if (!task.isSuccessful) {
//                            throw task.exception!!
//                        }
//                        return filepath.downloadUrl
//                    }
//                }).addOnCompleteListener(OnCompleteListener<Uri?> { task ->
//                    if (task.isSuccessful) {
//                        val downloadUrl = task.result
//                        myUrl = downloadUrl.toString()
//                        val messageTextBody: MutableMap<String, Any> = HashMap<String, Any>()
//                        messageTextBody["message"] = myUrl
//                        messageTextBody["name"] ?: fileuri!!.lastPathSegment
//                        messageTextBody["type"] = checker
//                        messageTextBody["from"] ?: messageSenderId
//                        messageTextBody["to"] ?: messageRecieverId
//                        messageTextBody["messageID"] ?: messagePushID
//                        messageTextBody["time"] ?: savecurrentTime
//                        messageTextBody["date"] ?: savecurrentDate
//                        val messageBodyDetails: MutableMap<String, Any> = HashMap<String, Any>()
//                        messageBodyDetails["$messageSenderRef/$messagePushID"] = messageTextBody
//                        messageBodyDetails["$messageReceiverRef/$messagePushID"] = messageTextBody
//                        RootRef!!.updateChildren(messageBodyDetails).addOnCompleteListener(OnCompleteListener { task ->
//                            if (task.isSuccessful) {
//                                loadingBar!!.dismiss()
//                            } else {
//                                loadingBar!!.dismiss()
//                                Toast.makeText(
//                                    this@ChatActivity,
//                                    "Error:",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            }
//                            messagesentinput!!.setText("")
//                        })
//                    }
//                })
            } else {
                loadingBar!!.dismiss()
                Toast.makeText(requireContext(), "please select file", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun displayLastSeen() {
        RootRef!!.child("Users").child(messageRecieverId!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        nameIs = dataSnapshot.child("name").value.toString()
                        imageIs = dataSnapshot.child("image").value.toString()
                        val myGenderIs = dataSnapshot.child("gender").value
                        if (myGenderIs != null) {
                            //  genderIs = myGenderIs.toString().toInt()
                        } else {
                            //   genderIs = 0
                        }

                        if (dataSnapshot.child("userState").hasChild("state")) {
                            val state =
                                dataSnapshot.child("userState").child("state").value.toString()
                            val date =
                                dataSnapshot.child("userState").child("date").value.toString()
                            val time =
                                dataSnapshot.child("userState").child("time").value.toString()
                            if (state == "online") {
                                userlastseen!!.text = "online"
                            } else if (state == "offline") {
                                userlastseen!!.text = "Last seen: $time"
                            }
                        } else {
                            userlastseen!!.text = "offline"
                        }
                        Log.d(TAG, "onDataChangeChatScreenOut: ${nameIs} ${imageIs}")
                        if (nameIs.equals("null")) {
                            binding.tvName.text = "GentleMan User"
                        } else {
                            binding.tvName.text =
                                CommonUtils.capitaliseOnlyFirstLetter(nameIs)

                        }

                        context?.let {
                            if (imageIs != null && imageIs != "") {
                                if (genderIs == 0 || genderIs == 1) {
                                    Glide.with(it)
                                        .load(ApiConstants.BASE_FILE + imageIs)
                                        .placeholder(com.app.gentlemanspa.R.drawable.profile_placeholder)
                                        .into(binding.ivProfile)
                                } else {
                                    binding.ivProfile.setImageResource(com.app.gentlemanspa.R.drawable.profile_placeholder)

                                }

                                if (imageIs.isNullOrEmpty()) {
                                    Glide.with(requireContext())
                                        .load(imageIs)
                                        .placeholder(com.app.gentlemanspa.R.drawable.profile_placeholder)
                                        .error(com.app.gentlemanspa.R.drawable.profile_placeholder)
                                        .into(binding.ivProfile)
                                } else {
                                    context?.let {
                                        Glide.with(it)
                                            .load(ApiConstants.BASE_FILE + imageIs)
                                            .placeholder(com.app.gentlemanspa.R.drawable.profile_placeholder)
                                            .error(com.app.gentlemanspa.R.drawable.profile_placeholder)
                                            .into(binding.ivProfile)
                                    }
                                }
                            }
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })

    }

    private fun usernameExists(username: String): Boolean {
        val fdbRefer = FirebaseDatabase.getInstance().getReference("Users/$username")
        return fdbRefer != null
    }

    private fun sendMessage() {
        if (myTxtMsg.isNullOrEmpty()) {
            binding.sendMsgCV.isEnabled = true
//            Toast.makeText(requireContext(), "Please enter message first..", Toast.LENGTH_SHORT)
//                .show()
        } else {
            RootRef?.child("Users")?.child(messageRecieverId.toString())
                ?.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val messageSenderRef = "Messages/$messageSenderId/$messageRecieverId"
                            val messageReceiverRef = "Messages/$messageRecieverId/$messageSenderId"
                            val Usermessagekeyref =
                                RootRef!!.child("Messages").child(messageSenderId!!).child(
                                    messageRecieverId!!
                                ).push()
                            val messagePushID = Usermessagekeyref.key
                            val messageTextBody: MutableMap<String, Any> =
                                java.util.HashMap<String, Any>()
                            messageTextBody["message"] = myTxtMsg
                            messageTextBody["type"] = "text"
                            messageTextBody["from"] = messageSenderId.toString()
                            messageTextBody["to"] = messageRecieverId.toString()
                            messageTextBody["messageID"] = messagePushID.toString()
                            messageTextBody["time"] = savecurrentTime.toString()
                            messageTextBody["date"] = savecurrentDate.toString()
                            Log.d(TAG, "sendMessage: $messageTextBody")
                            val messageBodyDetails: MutableMap<String, Any> =
                                java.util.HashMap<String, Any>()
                            messageBodyDetails["$messageSenderRef/$messagePushID"] = messageTextBody
                            messageBodyDetails["$messageReceiverRef/$messagePushID"] =
                                messageTextBody

                            RootRef!!.updateChildren(messageBodyDetails)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Log.d(TAG, "onDataChangeSendMessage: ${task.isSuccessful}")

                                        binding.sendMsgCV.isEnabled = true

                                        FirebaseDatabase.getInstance().reference.child("Users")
                                            .child(messageRecieverId.toString().trim())
                                            .child("fcm_token")
                                            .addListenerForSingleValueEvent(object :
                                                ValueEventListener {
                                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                                    usertoken =
                                                        dataSnapshot.getValue(String::class.java)
                                                    val profileCustomerData =
                                                        AppPrefs(requireContext()).getProfileCustomerData(
                                                            PROFILE_CUSTOMER_DATA
                                                        )
                                                    val myName =
                                                        "${profileCustomerData?.data?.firstName} ${profileCustomerData?.data?.lastName}"
//                                          notifyNotification()
                                                    sendPushNotification(
                                                        usertoken,
                                                        messageRecieverId.toString(),
                                                        messageSenderId.toString(),
                                                        "${
                                                            CommonUtils.capitaliseOnlyFirstLetter(
                                                                myName.toString()
                                                            )
                                                        } sent a message",
                                                        myTxtMsg
                                                    )
                                                    Log.d(
                                                        TAG,
                                                        "onDataChangeMyTextMsg: $myTxtMsg  $usertoken"
                                                    )

//FOR CONTACT SAVED FOR MESSAGE LIST======
                                                    if (contactsRef?.child(messageRecieverId.toString()) != null && contactsRef?.child(
                                                            messageSenderId.toString()
                                                        ) != null
                                                    ) {
                                                        // Toast.makeText(context, "both true", Toast.LENGTH_SHORT).show()
                                                    } else {
//                                    saveContactMsgList(
//                                        messageSenderId.toString(),
//                                        messageRecieverId.toString()
//                                    )
                                                    }
                                                }

                                                override fun onCancelled(databaseError: DatabaseError) {}
                                            })
                                        //   Toast.makeText(requireContext(), "${task.result}", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(
                                            requireActivity(), "Error:", Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    binding.chatMsgET.setText("")
                                }

                            val toParse: String =
                                savecurrentDate + " " + savecurrentTime // Results in "2-5-2012 20:43"

                            //val formatter = SimpleDateFormat("dd-MM-yyyy hh:mm a") // I assume d-M, you may refer to M-d for month-day instead.
                            val pattern = "dd/MM/yyyy hh:mm a"
                            val formatter = SimpleDateFormat(pattern, Locale.getDefault())
                            val date =
                                formatter.parse(toParse) // You will need try/catch around this
                            val millis = date.time

                            val contactHash: HashMap<String, Any> = HashMap()
                            contactHash.put("BlockStatus", false)
                            contactHash.put("Contact", "Saved")

                            val hashMapLatestMsg: HashMap<String, Any> = HashMap()
                            val msgDate = "$savecurrentDate"
                            val msgTime = "$savecurrentTime"
                            val msgTimeStamp = millis
                            val msgText = myTxtMsg
                            val isRead = false
                            hashMapLatestMsg.put("date", msgDate)
                            hashMapLatestMsg.put("time", msgTime)
                            hashMapLatestMsg.put("message", msgText)
                            hashMapLatestMsg.put("timeStamp", msgTimeStamp)
                            hashMapLatestMsg.put("is_read", isRead)
                            Log.d(
                                TAG,
                                "sendMessageLatestMessage: $messageRecieverId  $messageSenderId  $"
                            )
                            RootRef?.child("Contacts")?.child(messageRecieverId.toString())
                                ?.child(messageSenderId.toString())?.child("latest_message")
                                ?.updateChildren(hashMapLatestMsg)?.addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
//                        RootRef?.child("Contacts")?.child(messageSenderId.toString())?.child(messageRecieverId.toString())?.child("ContactStatus")
//                            ?.setValue(contactHash)?.addOnSuccessListener {
//                                Toast.makeText(context, "Contact Saved sender into receiver", Toast.LENGTH_SHORT).show()
//                            }
                                        Log.d(
                                            TAG,
                                            "sendMessageLatestMsgReceive: $msgText  $messageSenderId"
                                        )
                                    } else {
                                    }
                                }
                            RootRef?.child("Contacts")?.child(messageSenderId.toString())
                                ?.child(messageRecieverId.toString())?.child("latest_message")
                                ?.updateChildren(hashMapLatestMsg)?.addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        RootRef?.child("Contacts")
                                            ?.child(messageSenderId.toString())
                                            ?.child(messageRecieverId.toString())
                                            ?.child("ContactStatus")?.setValue(contactHash)
                                            ?.addOnSuccessListener {
//                                Toast.makeText(context, "Contact Saved receiver into Sender", Toast.LENGTH_SHORT).show()
                                            }
                                        Log.d(
                                            TAG,
                                            "sendMessageLatestMsgReceive: $msgText  $messageRecieverId"
                                        )
                                    } else {
                                    }
                                }
                        } else {
                            Toast.makeText(
                                requireContext(), "User Doesn't Exist!", Toast.LENGTH_SHORT
                            ).show()
                            // findNavController().navigate(R.id.action_chat_to_messages)

                            val navController = view?.let { Navigation.findNavController(it) }
                            navController?.navigateUp()
                            navController?.navigate(com.app.gentlemanspa.R.id.customerActivity)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
        }
    }

    private fun saveContactMsgList(messageSenderId: String, messageRecieverId: String) {
        contactsRef?.child(messageSenderId)?.child(messageRecieverId)?.child("Contacts")
            ?.setValue("Saved")?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    contactsRef?.child(messageSenderId)?.child(messageRecieverId)
                        ?.child("BlockStatus")?.setValue(false)?.addOnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(
                                    context, "Block Status Saved Successful", Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    context, "Block Status Saved failure", Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }
        contactsRef?.child(messageRecieverId)?.child(messageSenderId)?.child("Contacts")!!
            .setValue("Saved").addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        context, "New Contact Saved", Toast.LENGTH_SHORT
                    ).show()
                }
            }

    }

    /* private fun initObserver() {
         viewModelChat.responseUnBlockUserPostProblem.observe(viewLifecycleOwner){
             if(it.data?.status==200){
                 if(it?.data?.success==true){

                     FirebaseDatabase.getInstance().reference.child("Contacts").child(messageSenderId.toString()).child(messageRecieverId.toString()).child("ContactStatus")
                         .child("BlockStatus").setValue(false).addOnSuccessListener {
 //                            Toast.makeText(context, "Block Status false2", Toast.LENGTH_SHORT).show()
                         }
                     alertUnBlockDialogBinding?.unblockTV?.isEnabled =true

                 }else{
                     showSnackbarMessage("${it.data?.message}")
                 }
             }else{
                 showSnackbarMessage("${it.errorMsg}")
             }
         }


         viewModelChat.chatReportTypeListResponse.observe(viewLifecycleOwner) { user ->
             if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                 when (user.status) {
                     Status.SUCCESS -> {
                         hideLoading()
                         if (user.data != null) {
                             if (user?.data?.success == true) {
                                 binding.progress.visibility = View.INVISIBLE
                                 setData(user.data?.data)
                             } else {
                                 binding.progress.visibility = View.INVISIBLE
                             }
                         }
                     }
                     else -> {
                         binding.progress.visibility = View.VISIBLE
                         hideLoading()
                     }
                 }
             }
         }


         viewModelChat.responseReportProfile.observe(viewLifecycleOwner) { user ->
             if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                 when (user.status) {
                     Status.SUCCESS -> {
                         hideLoading()
                         if (user.data != null) {
                             if (user?.data?.success == true) {
                                 binding.progress.visibility = View.GONE
                                 Toast.makeText(context, "${user.data?.message}", Toast.LENGTH_SHORT).show()
                             } else {
                                 binding.progress.visibility = View.GONE
                                 Toast.makeText(context, "${user.data?.message}", Toast.LENGTH_SHORT).show()
                             }
                         }
                     }
                     else -> {
                         Toast.makeText(context, "${user.errorMsg}", Toast.LENGTH_SHORT).show()
                         binding.progress.visibility = View.VISIBLE
                     }
                 }
             }
         }

         viewModelChat.responseBlockUser.observe(viewLifecycleOwner){
             if(it.status==200){
                 if(it.data?.status==200){
                     if(it.data?.success==true){
                         Toast.makeText(context, "${it.data?.message}", Toast.LENGTH_SHORT).show()

                         RootRef?.child("Contacts")?.child(messageSenderId.toString())?.child(messageRecieverId.toString())?.child("ContactStatus")
                             ?.child("BlockStatus")?.setValue(true)?.addOnSuccessListener {
                                 //Toast.makeText(context, "Block Status true2", Toast.LENGTH_SHORT).show()
                             }
                     }else{
                         Toast.makeText(context, "${it.data?.message}", Toast.LENGTH_SHORT).show()
                     }
                 }else{
                     Toast.makeText(context, "${it.data?.message}", Toast.LENGTH_SHORT).show()
                 }
             }else{
                 Toast.makeText(context, "${it.errorMsg}", Toast.LENGTH_SHORT).show()
             }
         }

     }*/

    /*  private fun showReportDialog() {
          binding.progress.visibility = View.VISIBLE
          viewModelChat.reportTypeListData()

      }*/


    /*   @SuppressLint("ResourceAsColor")
       private fun setData(data: ArrayList<ResponseReportTypeListApi.ReportTypeListData>?) {
           dialogBuilder = AlertDialog.Builder(context)
           val alertReportLayoutBinding: AlertReportLayoutBinding =
               AlertReportLayoutBinding.inflate(LayoutInflater.from(context))
           dialogBuilder!!.setView(alertReportLayoutBinding.root)

           alertReportLayoutBinding.titleDescET.text!!.trim().toString()

           for(i in 0 until data?.size!!){
               val dialogData =data.get(i)
               if(dialogData.reportType.equals("Violence")){
                   alertReportLayoutBinding.violenceTV.text =dialogData.reportType
                   alertReportLayoutBinding.violenceTV1.text =dialogData.reportType

                   alertReportLayoutBinding.violenceCV.setOnClickListener {
                       alertReportLayoutBinding.violenceCV1.visibility = View.VISIBLE
                       alertReportLayoutBinding.harassmentCV1.visibility = View.INVISIBLE
                       alertReportLayoutBinding.fakeNewsCV1.visibility = View.INVISIBLE
                       alertReportLayoutBinding.spamCV1.visibility = View.INVISIBLE
   //                reportType ="Violence"
                       if (dialogData.reportType.equals("Violence")) {
                           reportId = dialogData.reportId!!
                           reportType =dialogData.reportType.toString()

                       }
                   }
               }
               if(dialogData.reportType.equals("Harassment")){
                   alertReportLayoutBinding.harassmentTV.text =dialogData.reportType
                   alertReportLayoutBinding.harassmentTV1.text =dialogData.reportType
                   alertReportLayoutBinding.harassmentCV.setOnClickListener {
                       alertReportLayoutBinding.violenceCV1.visibility = View.INVISIBLE
                       alertReportLayoutBinding.harassmentCV1.visibility = View.VISIBLE
                       alertReportLayoutBinding.fakeNewsCV1.visibility = View.INVISIBLE
                       alertReportLayoutBinding.spamCV1.visibility = View.INVISIBLE
                       reportId = dialogData.reportId!!
                       reportType =dialogData.reportType.toString()
                   }
               }
               if(dialogData.reportType.equals("Fake News")){
                   alertReportLayoutBinding.fakeNewsTV.text =dialogData.reportType
                   alertReportLayoutBinding.fakeNewsTV1.text =dialogData.reportType
                   alertReportLayoutBinding.fakeNewsCV.setOnClickListener {
                       alertReportLayoutBinding.violenceCV1.visibility = View.INVISIBLE
                       alertReportLayoutBinding.harassmentCV1.visibility = View.INVISIBLE
                       alertReportLayoutBinding.fakeNewsCV1.visibility = View.VISIBLE
                       alertReportLayoutBinding.spamCV1.visibility = View.INVISIBLE
                       reportId = dialogData.reportId!!
                       reportType =dialogData.reportType.toString()
                   }
               }
               if(dialogData.reportType.equals("Spam")){
                   alertReportLayoutBinding.spamTV.text =dialogData.reportType
                   alertReportLayoutBinding.spamTV1.text =dialogData.reportType
                   alertReportLayoutBinding.spamCV.setOnClickListener {
                       alertReportLayoutBinding.violenceCV1.visibility = View.INVISIBLE
                       alertReportLayoutBinding.harassmentCV1.visibility = View.INVISIBLE
                       alertReportLayoutBinding.fakeNewsCV1.visibility = View.INVISIBLE
                       alertReportLayoutBinding.spamCV1.visibility = View.VISIBLE
                       reportId = dialogData.reportId!!
                       reportType =dialogData.reportType.toString()
                   }
                   Log.d(ContentValues.TAG, "setDataSpamReport: $reportId")
               }
               reportType =""
           }


           alertReportLayoutBinding.reportTV.setOnClickListener({
               Log.d(ContentValues.TAG, "setDataReportProblem: $reportId")
               problemDesc = alertReportLayoutBinding.titleDescET.text.toString()
               if(reportType !=""){
                   if(problemDesc !=""){
                       viewModelChat.reportUserProfile(reportId,messageRecieverId,problemDesc)
   //                    viewModelChat.reportData(reportId,problemDesc,0)
                       alertDialog?.dismiss()
                   }else{
                       Toast.makeText(requireContext(),"Please write your problem!", Toast.LENGTH_SHORT).show()
                   }
               }else{
                   Toast.makeText(requireContext(),"You can report the post after selecting a problem!!",
                       Toast.LENGTH_SHORT).show()
               }

           })
           alertReportLayoutBinding.cancelTV.setOnClickListener {
               alertDialog?.dismiss()
           }



           alertDialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation;
           alertDialog = dialogBuilder!!.create()


           val displayMetrics = DisplayMetrics()
           if (alertDialog != null && alertDialog!!.window != null) {
               alertDialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
           }
           alertDialog!!.window?.attributes?.windowAnimations =
               com.chaos.view.R.style.AlertDialog_AppCompat
           // alertDialog1.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics)
           val displayWidth: Int = displayMetrics.widthPixels
           val layoutParams: WindowManager.LayoutParams = WindowManager.LayoutParams()
           layoutParams.copyFrom(alertDialog!!.window!!.attributes)
           // alertDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
           val dialogWindowWidth = (displayWidth * 1.05f).toInt()
           layoutParams.width = dialogWindowWidth
           layoutParams.gravity = Gravity.CENTER
           alertDialog!!.window!!.attributes = layoutParams
           alertDialog!!.show()
           alertDialog!!.setCancelable(true)
       }*/

    //SEND NOTIFICATIONS ===
    fun sendPushNotification(
        token: String?, receiverId: String, senderId: String, title: String, body: String
    ) {
        val url = "https://fcm.googleapis.com/fcm/send"
        Log.d(TAG, "sendPushNotificationLog: $title  $body")
        val bodyJson = JSONObject()
        bodyJson.put("notification", JSONObject().also {
            it.put("title", title)
            it.put("body", body)
            it.put("badge", 1)
        })

        bodyJson.put("data", JSONObject().also {
            it.put("title", title)
            it.put("body", body)
            it.put("fromNotification", "messageScreen")
            it.put("receiverId", receiverId)
            it.put("senderId", senderId)
        })
        bodyJson.put("to", token)
        bodyJson.put("priority", "high")


//            bodyJson.put("notification",
//            JSONObject().also {
//                it.put("title", title)
//                it.put("body", body)
//            })
        Log.d(TAG, "sendPushNotificationJSON: $bodyJson")
//        bodyJson.put("data", JSONObject(data))

        val request = Request.Builder().url(url).addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "key=$serverKey").post(
                bodyJson.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
            ).build()

        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                println("Received data: ${response.body?.string()}")
                Log.d(TAG, "onResponseNotificationSuccess: ${response.body.toString()}")
                Log.d(TAG, "onResponseNotificationSuccessMsg:  ${response.message}")
            }

            override fun onFailure(call: Call, e: IOException) {
                println(e.message.toString())
                Log.d(
                    TAG,
                    "onResponseNotificationfailure: ${response?.body.toString()}  ${e.message.toString()}"
                )

            }
        })
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.ivArrowBack->{
                  findNavController().popBackStack()
               // activity?.onBackPressedDispatcher?.onBackPressed()

            }
            /*  binding.ivMessageSend -> {
                  //doChat()
              }*/
        }
    }


}