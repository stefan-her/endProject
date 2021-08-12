package be.stefan.accessnfc.models

import android.content.Context
import android.content.SharedPreferences

class SharedPref(context: Context) {

    private val PREF_NAME = "loginUser"
    private val PREF_USER_EMAIL = "email"
    private val PREF_USER_PWD = "pwd"
    private val PREF_USER_FIRSTNAME = "firstname"
    private val PREF_USER_LASTNAME = "lastname"
    private val PREF_USER_UUID = "uuid"

    private val pref : SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val values : MutableMap<String, String> = mutableMapOf()

    fun getLoginUser(): MutableMap<String, String> {
        pref.getString(PREF_USER_EMAIL, null)?.let { values.put(PREF_USER_EMAIL, it) }
        pref.getString(PREF_USER_PWD, null)?.let { values.put(PREF_USER_PWD, it) }
        pref.getString(PREF_USER_FIRSTNAME, null)?.let { values.put(PREF_USER_FIRSTNAME, it) }
        pref.getString(PREF_USER_LASTNAME, null)?.let { values.put(PREF_USER_LASTNAME, it) }
        pref.getString(PREF_USER_UUID, null)?.let { values.put(PREF_USER_UUID, it) }
        return values
    }

    fun setLoginUser(values : Map<String, String>) {
        val editor = pref.edit()
        editor.putString(PREF_USER_EMAIL, values[PREF_USER_EMAIL])
        editor.putString(PREF_USER_PWD, values[PREF_USER_PWD])
        editor.putString(PREF_USER_FIRSTNAME, values[PREF_USER_FIRSTNAME])
        editor.putString(PREF_USER_LASTNAME, values[PREF_USER_LASTNAME])
        editor.putString(PREF_USER_UUID, values[PREF_USER_UUID])
        editor.apply()
    }
}