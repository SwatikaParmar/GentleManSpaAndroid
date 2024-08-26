package com.app.gentlemanspa.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Environment
import android.provider.OpenableColumns
import android.provider.Settings
import android.text.Editable
import android.text.Spannable
import android.text.Spanned
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.LinkMovementMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.ClickableSpan
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import com.app.gentlemanspa.R
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


object CommonFunctions {

    private lateinit var alertDialog: AlertDialog

    fun spannableAuthString(
        span: Spannable,
        start: Int,
        end: Int,
        textView: TextView,
        myClickableSpan: ClickableSpan
    ) {

        span.setSpan(myClickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = span
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.highlightColor = Color.TRANSPARENT
    }

    fun startSpaceEditText(editText: EditText) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (charSequence.isNotEmpty()) {
                    if ((charSequence[0].toString() + "").equals(" ", ignoreCase = true)) {
                        val oldPass = editText.text.toString()
                        editText.setText(oldPass.replace(" ", ""))
                    }
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })
    }


    fun getError(throwable: Throwable?): String? {
        return when (throwable) {
            is UnknownHostException -> {
                "No Internet Connection"
            }

            is SocketTimeoutException -> {
                "Server is not responding. Please try again"
            }

            is ConnectException -> {
                "Failed to connect server"
            }



            else -> {
                throwable?.localizedMessage
            }
        }
    }





     fun togglePasswordVisibility( passwordEditText: EditText, showPasswordImageView: ImageView, isPasswordVisible: Boolean) {
        if (isPasswordVisible) {
            // Change to hide password icon
            showPasswordImageView.setImageResource(R.drawable.show_password)
            passwordEditText.transformationMethod =
                HideReturnsTransformationMethod.getInstance()
        } else {
            showPasswordImageView.setImageResource(R.drawable.hide_password)
            passwordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
        }

        passwordEditText.setSelection(passwordEditText.text?.length ?: 0)
    }

     fun prepareFilePart(partName: String, file: File): MultipartBody.Part {
        val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }

     fun getTextRequestBodyParams(value: String?): RequestBody? {
        return value?.toRequestBody("text/form-data".toMediaTypeOrNull())
    }



     fun goToAppSettings(requireContext: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", requireContext.packageName, null))
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        requireContext.startActivity(intent)
    }

    fun decimalRoundToInt(value :Double?):String {
        var roundoff =""

        if (value != null) {
            roundoff = if (value % 1 != 0.0){
                String.format("%.2f", value)
            }else {
                String.format("%.0f", value)
            }
        }
        return roundoff // 0.85
    }

    fun dateFormat(inputDateStr: String): String {
        val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", Locale.ENGLISH)
        val outputFormat: DateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
        val date = inputFormat.parse(inputDateStr)
        return date?.let { outputFormat.format(it) }.toString()


    }




        fun splitByCapitalLetters(input: String?): List<String> {
            val words = mutableListOf<String>()
            val currentWord = StringBuilder()

            input?.forEachIndexed { index, char ->
                // Check if the current character is a capital letter
                if (char.isUpperCase()) {
                    // If we have accumulated characters in the current word, add it to the list
                    if (currentWord.isNotEmpty()) {
                        words.add(currentWord.toString())
                        currentWord.clear()
                    }
                }
                // Append the current character to the current word
                currentWord.append(char)

                // If this is the last character, add the current word to the list
                if (index == input.length - 1) {
                    words.add(currentWord.toString())
                }
            }
            return words
        }


    fun uriToFile(uri: Uri, requireContext: Context, requireActivity: FragmentActivity): File? {
        val cursor = requireActivity.contentResolver.query(uri, null, null, null, null)
        return if (cursor != null && cursor.moveToFirst()) {
            val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            val name = cursor.getString(index)
            val file = File(requireContext.cacheDir, name)
            val inputStream = requireActivity.contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            cursor.close()
            inputStream?.close()
            outputStream.close()
            file
        } else {
            null
        }
    }

    @Throws(IOException::class)
    fun createImageFile(requireActivity: FragmentActivity): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir: File? =
            requireActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(imageFileName, ".jpg", storageDir)
    }

    fun compressImageFile(imageFile: File) {
        val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
        val outputStream = FileOutputStream(imageFile)
        bitmap.compress(
            Bitmap.CompressFormat.JPEG,
            80,
            outputStream
        ) // 80 is the compression quality
        outputStream.close()
    }


}


