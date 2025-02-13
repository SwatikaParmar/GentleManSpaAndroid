package com.app.gentlemanspa.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.app.gentlemanspa.ui.auth.activity.AuthActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import org.json.JSONObject


class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("MyFirebaseMessagingService", "New token: $token")
        // Send token to your server if needed
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        // Handle FCM messages here
        Log.d("MyFirebaseMessagingService", "remoteMessage data: ${remoteMessage.data}")
        // Extract notification data from the message
        val title = remoteMessage.notification?.title ?: "Default Title"
        val body = remoteMessage.notification?.body ?: "Default Message"
       /* val gson = Gson()
        val jsonOutput = gson.toJson(remoteMessage.data.toString())*/
        sendLocalNotification(title, body)
    }
    private fun sendLocalNotification(title: String, message: String) {
//        Log.e("CheckInTest", "target_model inside notification  -> ${JSONObject(body).getString("type")}")

        val channelId = "default_channel"
        val notificationId = 1001
        // Create an intent for the notification tap action
        val intent = Intent(this, AuthActivity::class.java) // Replace with your target activity
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        // Create a notification
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // Replace with your app icon
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        // Get the NotificationManager
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create a notification channel for Android 8.0 and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Default Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
        // Display the notification
        notificationManager.notify(notificationId, notificationBuilder.build())
    }
    override fun handleIntent(intent: Intent?) {
        super.handleIntent(intent)
         /*   if (intent != null) {
                if (intent.extras != null) {
                    val builder = RemoteMessage.Builder("FirebaseNotificationService")
                    for (key in intent.extras!!.keySet()) {
                        builder.addData(key!!, intent.extras?.getString(key))
                    }
                    //  onMessageReceived(builder.build())
                    Log.d("MyFirebaseMessagingService", "handleIntent: keys ${onMessageReceived(builder.build())}")

                } else {
                    super.handleIntent(intent)
                }
            }*/
        }


}
