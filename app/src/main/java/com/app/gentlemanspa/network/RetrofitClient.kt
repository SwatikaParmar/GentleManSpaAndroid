package com.app.gentlemanspa.network

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.app.gentlemanspa.base.MyApplication.Companion.context
import com.app.gentlemanspa.utils.AppPrefs

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitClient {

    private const val diskCacheSize = 10 * 1024 * 1024

    fun getClient(baseUrl: String?): Retrofit? {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(getHttpClient())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
    }

    private fun getHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()

        return OkHttpClient.Builder()

            //.cache(getCache())
            .build()
            .newBuilder()
            .addInterceptor(interceptor.setLevel(HttpLoggingInterceptor.Level.BODY))
            .addNetworkInterceptor(errorInterceptor)
            .addNetworkInterceptor(getNetworkInterceptor())
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    private fun getNetworkInterceptor(): Interceptor {
        return Interceptor { chain ->

            val request: Request
            val token = AppPrefs(context).getString("TOKEN")
            Log.e("TAG", "getNetworkInterceptor: "+token.toString() )

            request = if (!token.isNullOrEmpty()) {
                chain.request().newBuilder().addHeader(ApiConstants.KEY_AUTHORIZATION, "Bearer $token").build()

            } else {
                chain.request().newBuilder()
                    .build()
            }

            chain.proceed(request)


        }

    }





    private val  errorInterceptor = Interceptor { chain ->
        val request: Request = chain.request()
        val response = chain.proceed(request)
        when (response.code) {

            401-> {
                Handler(Looper.getMainLooper()).post {
                    val intent = Intent("SessionExpired")
                   // context.sendBroadcast(intent)
                }
            }


            else -> {

                Handler(Looper.getMainLooper()).post {
                    val intent = Intent("Message")
                   // context.sendBroadcast(intent)
                }


            }

        }
        response
    }



}