package com.app.gentlemanspa.utils.updateStatus.viewModel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.utils.updateStatus.model.UpdateFCMTokenRequest
import com.app.gentlemanspa.utils.updateStatus.model.UpdateOnlineStatusRequest
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException

class UpdateStatusViewModel(private var initialRepository: InitialRepository) : AndroidViewModel(
    Application()
) {

    // Function to update the user's status
    fun updateOnlineStatusApi(userId: String, status: Boolean) {
        val request= UpdateOnlineStatusRequest(userId,status)
        viewModelScope.launch {
            try {
                initialRepository.updateOnlineStatus(request)
                    .onStart {
                    }
                    .onCompletion {

                    }
                    .catch { exception ->
                        // Catch any errors, show them as Toast messages
                        when (exception) {
                            is HttpException -> {
                                try {
                                    val errorBody = exception.response()?.errorBody()?.string()
                                    val errorMessage = if (!errorBody.isNullOrEmpty()) {
                                        val jsonError = JSONObject(errorBody)
                                        jsonError.optString("messages", "Unknown HTTP error")
                                    } else {
                                        "Unknown HTTP error"
                                    }
                                    // Show the error message in a Toast
                                    Log.d("updateStatus","errorMessage${errorMessage}")

                                  //  Toast.makeText(getApplication(), errorMessage, Toast.LENGTH_SHORT).show()
                                } catch (e: Exception) {
                                    Toast.makeText(getApplication(), e.message ?: "Error parsing error response", Toast.LENGTH_SHORT).show()
                                }
                            }
                            else -> {
                                Toast.makeText(getApplication(), exception.message ?: "Unknown error", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    .collect { response ->
                        // Handle successful response
                        if(response?.statusCode == 200) {
                            Log.d("updateStatus","success->Status updated successfully")
                           // Toast.makeText(getApplication(), "Status updated successfully", Toast.LENGTH_SHORT).show()
                        } else {
                      //      Toast.makeText(getApplication(), response?.messages ?: "Unknown error", Toast.LENGTH_SHORT).show()
                            Log.d("updateStatus","response error message${response?.messages}")

                        }
                    }
            } catch (exception: Exception) {
                Log.d("updateStatus","exception->${exception.message}")
                    //Toast.makeText(getApplication(), exception.message ?: "Unknown error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun updateFCMTokenApi(token:String) {
        val request= UpdateFCMTokenRequest(token)
        viewModelScope.launch {
            try {
                initialRepository.updateFCMToken(request)
                    .onStart {
                    }
                    .onCompletion {

                    }
                    .catch { exception ->
                        // Catch any errors, show them as Toast messages
                        when (exception) {
                            is HttpException -> {
                                try {
                                    val errorBody = exception.response()?.errorBody()?.string()
                                    val errorMessage = if (!errorBody.isNullOrEmpty()) {
                                        val jsonError = JSONObject(errorBody)
                                        jsonError.optString("messages", "Unknown HTTP error")
                                    } else {
                                        "Unknown HTTP error"
                                    }
                                    // Show the error message in a Toast
                                    Log.d("updateFCMToken","errorMessage${errorMessage}")

                                    //  Toast.makeText(getApplication(), errorMessage, Toast.LENGTH_SHORT).show()
                                } catch (e: Exception) {
                                    Toast.makeText(getApplication(), e.message ?: "Error parsing error response", Toast.LENGTH_SHORT).show()
                                }
                            }
                            else -> {
                                Toast.makeText(getApplication(), exception.message ?: "Unknown error", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    .collect { response ->
                        // Handle successful response
                        if(response?.statusCode == 200) {
                            Log.d("updateFCMToken","success->FCM Token updated successfully")
                            // Toast.makeText(getApplication(), "Status updated successfully", Toast.LENGTH_SHORT).show()
                        } else {
                            //      Toast.makeText(getApplication(), response?.messages ?: "Unknown error", Toast.LENGTH_SHORT).show()
                            Log.d("updateFCMToken","response error message${response?.messages}")

                        }
                    }
            } catch (exception: Exception) {
                Log.d("updateFCMToken","exception->${exception.message}")
                //Toast.makeText(getApplication(), exception.message ?: "Unknown error", Toast.LENGTH_SHORT).show()
            }
        }
    }
}


