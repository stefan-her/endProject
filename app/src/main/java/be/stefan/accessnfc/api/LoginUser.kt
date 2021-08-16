package be.stefan.accessnfc.api

import android.app.Activity
import android.content.ComponentCallbacks
import android.content.Context
import android.util.Log
import be.stefan.accessnfc.R
import be.stefan.accessnfc.fragments.ConnectFragment
import be.stefan.accessnfc.fragments.MeetingFragment
import be.stefan.accessnfc.models.SharedPref
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.nio.charset.Charset


class LoginUser(context: Context, lambda : () -> Unit) {

    private val URL_LOGINUSER = "/apiTest/loginUser.php/"
    val lambdaParam = lambda
    private var url : String
    init {
        val sharedPref = SharedPref("accessApp", context)
        val accessApp = sharedPref.getAppUrl()
        url = accessApp + URL_LOGINUSER
        request(context)
    }
    private fun request(context: Context) {

        val sharedPref = SharedPref("loginUser", context)
        val loginUser = sharedPref.getLoginUser()

        val queue = Volley.newRequestQueue(context)
        val requestBody = "email=" + loginUser["email"] + "&pwd=" + loginUser["pwd"]
        val stringReq : StringRequest =
            object : StringRequest(Method.POST, url,
                Response.Listener { response ->
                    var strResp = response.toString()
                    Log.d("API", strResp)
                        if(strResp.trim() == "[]") {
                            val sharedPref = SharedPref("loginUser", context)
                            sharedPref.clearNode()
                        } else {
                            this@LoginUser.lambdaParam.invoke()
                        }
                    },
                    Response.ErrorListener { error ->
                        Log.d("API error------>", error.toString())
                    }
                ){
                    override fun getBody(): ByteArray {
                        return requestBody.toByteArray(Charset.defaultCharset())
                    }
                }
        queue.add(stringReq)
    }
}