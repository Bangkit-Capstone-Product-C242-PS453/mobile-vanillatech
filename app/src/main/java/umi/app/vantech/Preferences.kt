package umi.app.vantech

import android.content.Context
import android.content.SharedPreferences

class Preferences (val context : Context) {
    companion object{
        const val PREF = "USER_PREF"
    }

    val sharedPref = context.getSharedPreferences(PREF, 0)

    fun setValues(key: String, value: String){
        val editor : SharedPreferences.Editor = sharedPref.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getValues(key:String) :String? {
        return sharedPref.getString(key,"")
    }

    fun clearPreferences(){
        sharedPref.edit().clear().apply()
    }
}