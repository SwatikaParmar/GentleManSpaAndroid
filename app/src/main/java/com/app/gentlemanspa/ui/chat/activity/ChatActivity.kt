package com.app.gentlemanspa.ui.chat.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.app.gentlemanspa.R
import com.app.gentlemanspa.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {
    lateinit var binding:ActivityChatBinding
    private lateinit var navController: NavController
    private lateinit var navHost: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
        passDataToMessageFragment()
    }

    private fun initUI() {
        navHost = supportFragmentManager.findFragmentById(R.id.chatContainer) as NavHostFragment
        navController = navHost.navController
        val navInflater = navController.navInflater
        val graph = navInflater.inflate(R.navigation.chat)
        navController.graph = graph
    }

    private fun passDataToMessageFragment() {
        if (intent.extras !=null){
            val userId = intent.getStringExtra("userId")
            Log.d("userId", "Received userId: $userId")
            val bundle = Bundle().apply {
                putString("userId", userId)
            }
            navController.navigate(R.id.messagesFragment, bundle)
        }else{
            Log.d("userId", "Received username: null")

        }

    }

    @Suppress("DEPRECATION")
    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        super.onBackPressed()
        /* val fragmentPosition = navHost.childFragmentManager.fragments[0]
         if (fragmentPosition is LoginFragment) {
             closeApp()
         } else {
             super.onBackPressed()
         }*/
        finish()

    }
}