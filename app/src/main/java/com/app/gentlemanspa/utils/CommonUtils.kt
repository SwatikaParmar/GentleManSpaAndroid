package com.app.gentlemanspa.utils

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

object CommonUtils {

    fun fileToMultipart(file :String,name :String):MultipartBody.Part{
        val myFile =File(file)
        var requestBody =myFile.asRequestBody("image/jpg".toMediaTypeOrNull())
        var multipartBody =MultipartBody.Part.Companion.createFormData(name,myFile.name,requestBody)
        return multipartBody
    }


    fun getBitmapFromURL(src: String?): Bitmap? {
        return try {
            val url = URL(src)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.setDoInput(true)
            connection.connect()
            val input: InputStream = connection.getInputStream()
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            // Log exception
            null
        }
    }

    fun capitaliseOnlyFirstLetter(data :String):String{
       return  data.substring(0, 1).toUpperCase() + data.substring(1).toLowerCase()
    }

    fun timeFormat(date :String,time :String):String {
        val stringDate ="$date $time"
        val df = SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.ENGLISH)
        df.timeZone = TimeZone.getTimeZone("UTC")
        val date : Date =df.parse(stringDate)
        df.timeZone = TimeZone.getDefault()
        val formattedDate =df.format(date)
        val timeIs = SimpleDateFormat("dd-MM-yyyy").format(date)
        Log.d(ContentValues.TAG, "timeFormatTimeIs: $timeIs")
        return timeIs
    }
}