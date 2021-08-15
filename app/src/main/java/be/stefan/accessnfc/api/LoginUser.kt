package be.stefan.accessnfc.api

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO


class LoginUser(email: String, pwd: String, context: Context) {

    private val URL_LOGINUSER = "http://146.59.154.93/apiTest/loginUser.php/"

    init {
        CoroutineScope(IO).launch {
            request(context)
        }
    }


    suspend fun request(context: Context) {
        val queue = Volley.newRequestQueue(context)
        val url = URL_LOGINUSER

        val stringRequest = StringRequest(
                Request.Method.GET, url,
                { response ->
                    // Display the first 500 characters of the response string.
                    Log.d("Request----->", response)
                },
                {
                    Log.d("Request----->", "That didn't work!")
                }
        )

        queue.add(stringRequest)
    }
}

