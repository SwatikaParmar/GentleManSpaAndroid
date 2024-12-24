package com.app.gentlemanspa.utils

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.HashMap

@SuppressLint("SimpleDateFormat")
fun updateUserStatus(userIdIs:String,state: String) {
    val saveCurrentTime: String
    val saveCurrentDate: String
    val calendar = Calendar.getInstance()
    val currentDate = SimpleDateFormat("dd/MM/yyyy")
    saveCurrentDate = currentDate.format(calendar.time)
    val currentTime = SimpleDateFormat("hh:mm a")
    saveCurrentTime = currentTime.format(calendar.time)
    val hashMap = HashMap<String, Any>()
    hashMap["time"] = saveCurrentTime
    hashMap["date"] = saveCurrentDate
    hashMap["state"] = state
    FirebaseDatabase.getInstance().reference.child("Users").child(userIdIs)
        .addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    Log.d(TAG, "ChangeUserStatusOnStopExist: $snapshot")
                    FirebaseDatabase.getInstance().reference.child("Users").child(snapshot.key.toString()).child("userState")
                        .updateChildren(hashMap)
                }else{
                    Log.d(TAG, "ChangeUserStatusOnStopNotExist: $snapshot")
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }

        })

}