package com.app.gentlemanspa.utils


import android.content.Context
import android.content.SharedPreferences
import com.app.gentlemanspa.ui.professionalDashboard.fragment.profile.model.GetProfessionalDetailResponse

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val PROFESSIONAL_DETAIL_ID="Professional_Detail_Id"
class AppPrefs(private val ctx: Context) {

    private fun getPrefs(): SharedPreferences {
        return ctx.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE)
    }



    fun getInt(key: String?) :Int?{
        return getPrefs().getInt(key,0)
    }

    fun setInt(key : String?,value: Int){
        val edit = getPrefs().edit()
        edit.putInt(key, value)
        edit.apply()
    }
    fun setIntPref(key : String?,value: Int){
        val edit = getPrefs().edit()
        edit.putInt(key, value)
        edit.apply()
    }
    fun getIntPref(key: String?) :Int?{
        return getPrefs().getInt(key,0)
    }

    fun setString(key : String?,value: String?){
        val edit = getPrefs().edit()
        edit.putString(key, value)
        edit.apply()
    }

    fun getString(key: String?) :String?{
        return getPrefs().getString(key,"")
    }
    fun saveStringPref(key : String?,value: String?){
        val edit = getPrefs().edit()
        edit.putString(key, value)
        edit.apply()
    }

    fun getStringPref(key: String?) :String?{
        return getPrefs().getString(key,"")
    }

    fun setProfileProfessionalData(key: String?, value: GetProfessionalDetailResponse?) {
        val gson = Gson()
        val edit = getPrefs().edit()
        edit.putString(key, gson.toJson(value))
        edit.apply()
    }

    fun getProfileProfessionalData(key: String?) : GetProfessionalDetailResponse?{
        val gson = Gson()
        val type = object  : TypeToken<GetProfessionalDetailResponse>(){}.type
        return gson.fromJson(getPrefs().getString(key,""),type)
    }

    fun setProfileCustomerData(key: String?, value: GetProfessionalDetailResponse?) {
        val gson = Gson()
        val edit = getPrefs().edit()
        edit.putString(key, gson.toJson(value))
        edit.apply()
    }

    fun getProfileCustomerData(key: String?) : GetProfessionalDetailResponse?{
        val gson = Gson()
        val type = object  : TypeToken<GetProfessionalDetailResponse>(){}.type
        return gson.fromJson(getPrefs().getString(key,""),type)
    }



}
