package be.stefan.accessnfc.api

import android.content.Context
import android.util.Log
import be.stefan.accessnfc.models.SharedPref
import be.stefan.accessnfc.models.UpcomingMeetingItem
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class UpcomingMeetings(context: Context, lambda : (result: List<UpcomingMeetingItem>) -> Unit) {

    private val URL_UPCOMING = "/api/get_upcomingMeetings.php"
    private var url : String
    private val lambdaParam = lambda
    private lateinit var result : List<UpcomingMeetingItem>

    init {
        val sharedPref = SharedPref("accessApp", context)
        val accessApp = sharedPref.getAppUrl()
        url = accessApp + URL_UPCOMING
        request(context)
    }

    private fun request(context: Context) {
        val sharedPref = SharedPref("loginUser", context)
        val loginUser = sharedPref.getLoginUser()

        val queue = Volley.newRequestQueue(context)
        url += "?apikey=" + loginUser["apikey"]

        val stringReq = StringRequest(
            Request.Method.GET, url,
            { response ->
                var strResp = response.toString()
                Log.d("API", strResp)
                result = convert(strResp)

                this@UpcomingMeetings.lambdaParam.invoke(result)
            },
            { error ->
                Log.d("API error------>", error.toString())
            },
        )
        queue.add(stringReq)
    }

    private fun convert(json : String) : List<UpcomingMeetingItem> {
        var items  = mutableListOf<UpcomingMeetingItem>()

        val json : JSONObject = JSONObject(json)
        if(!json.has("empty")) {
            var array : JSONArray = json.getJSONArray("result")
            for (i in 0 until array.length()) {
                var el = array.getJSONObject(i)
                items.add(UpcomingMeetingItem(
                    el.getString("access"),
                    el.getString("begin"),
                    el.getString("end"),
                    el.getString("id_meeting"),
                    el.getString("prepared"),
                    el.getString("room"),
                    el.getString("title")
                ))
            }
         }

        return items.toList()
    }
}

