package be.stefan.accessnfc.api

import android.content.Context
import android.util.Log
import be.stefan.accessnfc.models.SharedPref
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.nio.charset.Charset

import org.json.JSONObject
import org.json.JSONTokener


class LoginUser(context: Context, lambda : (connect : Boolean) -> Unit) {

    private val URL_LOGINUSER = "/api/get_userInfos.php"
    private var url : String
    private val lambdaParam = lambda
    private var connect : Boolean = false

    init {
        val sharedPref = SharedPref("accessApp", context)
        val accessApp = sharedPref.getAppUrl()
        url = accessApp + URL_LOGINUSER
        request(context)
    }
    private fun request(context: Context) {

        val sharedPref = SharedPref("loginUser", context)
        val loginUser = sharedPref.getLoginUser()
        val sharedPrefUser = SharedPref("loginUser", context)

        val queue = Volley.newRequestQueue(context)
        val requestBody = "email=" + loginUser["email"] + "&pwd=" + loginUser["pwd"]
        val stringReq : StringRequest =
            object : StringRequest(Method.POST, url,
                Response.Listener { response ->
                    var strResp = response.toString()
                    Log.d("API", strResp)
                        if(strResp.trim() == "null") {
                            sharedPrefUser.clearNode()
                        } else {
                            val jsonObject = JSONTokener(response).nextValue() as JSONObject
                            if(!jsonObject.has("empty")) {
                                val map = mapOf(
                                    "firstname" to jsonObject.getString("firstname"),
                                    "lastname" to jsonObject.getString("lastname"),
                                    "apikey" to jsonObject.getString("apikey"),
                                    "email" to loginUser["email"],
                                    "pwd" to loginUser["pwd"]
                                )
                                sharedPrefUser.setLoginUser(map as Map<String, String>)
                                connect = true
                            } else {
                                sharedPrefUser.clearNode()
                            }
                        }
                        this@LoginUser.lambdaParam.invoke(connect)
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