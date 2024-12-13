package com.app.gentlemanspa.ui.auth.fragment.splash

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.app.gentlemanspa.R
import com.app.gentlemanspa.databinding.FragmentSplashBinding
import com.app.gentlemanspa.ui.customerDashboard.activity.CustomerActivity
import com.app.gentlemanspa.ui.professionalDashboard.activity.ProfessionalActivity
import com.app.gentlemanspa.utils.AppPrefs
import com.app.gentlemanspa.utils.FCM_TOKEN
import com.app.gentlemanspa.utils.areNotificationsEnabled
import com.app.gentlemanspa.utils.openNotificationSettings
import com.google.firebase.messaging.FirebaseMessaging


@Suppress("DEPRECATION")
class SplashFragment : Fragment() {

    private lateinit var binding: FragmentSplashBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        generateFCMToken()
        checkAndRequestNotificationPermission()
       // initUI()
    }

    private fun generateFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }
            // Get the new FCM token
            val token = task.result
            Log.d("FCM", "FCM Token: $token")
            if (isAdded){
                AppPrefs(requireContext()).saveStringPref(FCM_TOKEN, token)
            }

        }
    }

    private fun initUI() {
        lifecycleScope.launchWhenResumed {
            Handler(Looper.getMainLooper()).postDelayed({
                val token = AppPrefs(requireContext()).getString("TOKEN").toString()
                val role = AppPrefs(requireContext()).getString("ROLE").toString()

                when {
                    token.isNotEmpty() && role == "Customer" -> {
                     moveToCustomerHome()
                    }

                    token.isNotEmpty() && role == "Professional" -> {
                       moveToProfessionalHome()
                    }

                    else -> {
                        moveToLogin()
                    }
                }


            }, 1500)

        }
    }

    private fun moveToCustomerHome() {
        startActivity(Intent(requireContext(), CustomerActivity::class.java))
        requireActivity().finishAffinity()
    }

    private fun moveToProfessionalHome() {
        startActivity(
            Intent(
                requireContext(),
                ProfessionalActivity::class.java
            )
        )
        requireActivity().finishAffinity()
    }

    private fun moveToLogin(){
    findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
}
    private fun checkAndRequestNotificationPermission() {
        if (isAdded){
            // Only request the permission on Android 13+
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(
                        requireActivity(),
                        android.Manifest.permission.POST_NOTIFICATIONS
                    ) != android.content.pm.PackageManager.PERMISSION_GRANTED
                ) {
                    // Show rationale if needed
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            requireActivity(),
                            android.Manifest.permission.POST_NOTIFICATIONS
                        )
                    ) {
                        showPermissionRationale()
                    } else {
                        requestNotificationPermission()
                    }
                } else {
                    proceedToNextScreen()
                }
            } else {
                if (!areNotificationsEnabled(requireContext())) {
                    showSettingDialog()
                }else{
                    Log.d("SplashNotification","inside else part of areNotificationsEnabled")
                    proceedToNextScreen()
                }
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun showPermissionRationale() {
        AlertDialog.Builder(requireActivity())
            .setTitle("Enable Notifications")
            .setMessage("Stay updated with the latest alerts and updates. Allow notifications?")
            .setPositiveButton("Allow") { dialogInterface, _ ->
                dialogInterface.dismiss()
                requestNotificationPermission()
            }
            .setNegativeButton("No, thanks") { _, _ ->
                proceedToNextScreen()
            }
            .show()
    }
    private fun showSettingDialog() {
        AlertDialog.Builder(requireActivity())
            .setTitle("Notification Permission")
            .setMessage("Notification permission is required. Please allow notification permission from settings.")
            .setPositiveButton("Allow") { dialogInterface, _ ->
                dialogInterface.dismiss()
                openNotificationSettings(requireContext())
            }
            .setNegativeButton("No, thanks") { _, _ ->
                proceedToNextScreen()
            }.show()
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestNotificationPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
            REQUEST_NOTIFICATION_PERMISSION
        )
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            proceedToNextScreen()
        }
    }
    private fun proceedToNextScreen() {
        initUI()
    }

    override fun onResume() {
        super.onResume()
        Handler(Looper.getMainLooper()).postDelayed({
            checkAndRequestNotificationPermission()
        }, 4000)
    }
    companion object{
        private const val REQUEST_NOTIFICATION_PERMISSION = 1001

    }
}