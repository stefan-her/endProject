package be.stefan.accessnfc.models

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class SharedPref(node: String, context: Context) {

    private val PREF_USER_EMAIL = "email"
    private val PREF_USER_PWD = "pwd"
    private val PREF_USER_FIRSTNAME = "firstname"
    private val PREF_USER_LASTNAME = "lastname"
    private val PREF_USER_APIKEY = "apikey"
    private val PREF_ACCESSAPP = "url"

    private val pref : SharedPreferences = context.getSharedPreferences(node, Context.MODE_PRIVATE)
    private val values : MutableMap<String, String> = mutableMapOf()

    fun getLoginUser(): MutableMap<String, String> {
        pref.getString(PREF_USER_EMAIL, null)?.let { values.put(PREF_USER_EMAIL, it) }
        pref.getString(PREF_USER_PWD, null)?.let { values.put(PREF_USER_PWD, it) }
        pref.getString(PREF_USER_FIRSTNAME, null)?.let { values.put(PREF_USER_FIRSTNAME, it) }
        pref.getString(PREF_USER_LASTNAME, null)?.let { values.put(PREF_USER_LASTNAME, it) }
        pref.getString(PREF_USER_APIKEY, null)?.let { values.put(PREF_USER_APIKEY, it) }
        return values
    }

    fun setLoginUser(values : Map<String, String>) {
        val editor = pref.edit()
        values[PREF_USER_EMAIL]?.let { editor.putString(PREF_USER_EMAIL, values[PREF_USER_EMAIL]) }
        values[PREF_USER_PWD]?.let { editor.putString(PREF_USER_PWD, values[PREF_USER_PWD]) }
        values[PREF_USER_FIRSTNAME]?.let { editor.putString(PREF_USER_FIRSTNAME, values[PREF_USER_FIRSTNAME]) }
        values[PREF_USER_LASTNAME]?.let { editor.putString(PREF_USER_LASTNAME, values[PREF_USER_LASTNAME]) }
        values[PREF_USER_APIKEY]?.let { editor.putString(PREF_USER_APIKEY, values[PREF_USER_APIKEY]) }
        editor.apply()
    }

    fun getAppUrl(): String? {
        return  pref.getString(PREF_ACCESSAPP, null)
    }

    fun setAppUrl(url : String) {
        val editor = pref.edit()
        editor.putString(PREF_ACCESSAPP, url)
        editor.apply()
    }

    fun clearNode() {
        val editor = pref.edit()
        editor.clear()
        editor.apply()
    }
}