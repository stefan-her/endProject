package be.stefan.accessnfc.fragments

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.text.InputType.TYPE_CLASS_TEXT
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import be.stefan.accessnfc.R
import be.stefan.accessnfc.api.LoginUser
import be.stefan.accessnfc.models.SharedPref
import com.android.volley.Request
import com.android.volley.VolleyLog
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class WelcomeFragment : Fragment() {

    lateinit var progressBar : ProgressBar
    //val TYPEFRAGS : List<String> = listOf("connection", "meeting")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    //http://146.59.154.93
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        var v : View = inflater.inflate(R.layout.fragment_welcome, container, false)
        progressBar = v.findViewById(R.id.progressBar)
        progressBar.visibility = View.GONE
        checkNetworkAvailable()
        return v
    }

    private fun checkNetworkAvailable() {
        val connect = isNetworkAvailable()
        Log.i("internet ou pas ----->", connect.toString())
        if(!connect) { alertDialogNetwork(requireContext()) }
        else { checkAccessApp() }
    }

    private fun isNetworkAvailable() : Boolean {
        var result = false
        val cm = activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = cm.activeNetwork
        val activeNetwork = (cm.getNetworkCapabilities(networkCapabilities))
        if (activeNetwork != null) {
            result = when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }
        return result
    }

    private fun alertDialogNetwork(context: Context) {
        val builder : AlertDialog.Builder = AlertDialog.Builder(context)

        builder.setMessage(R.string.connectionlost)
        builder.setCancelable(true)

        builder.setPositiveButton(R.string.activeConnection) {
            dialog, id -> activeNetwork()
        }
        builder.setNegativeButton(R.string.close) {
            dialog, id -> dialog.cancel()
            requireActivity().finish()
        }
        val alert = builder.create()
        alert.setTitle(R.string.connection)
        alert.show()
    }

    private fun activeNetwork() {
        val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
        resultLauncher.launch(intent)
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        checkNetworkAvailable()
    }

    private fun checkAccessApp() {
        progressBar.visibility = View.VISIBLE

        val sharedPref = SharedPref("accessApp", requireActivity())
        val accessApp = sharedPref.getAppUrl()

        if (accessApp == null) { alertDialogAccessApp(requireContext()) }
        else { validRemoteAdd(accessApp, requireContext()) }
    }

    private fun alertDialogAccessApp(context: Context) {
        val builder : AlertDialog.Builder = AlertDialog.Builder(context)

        builder.setMessage(getString(R.string.adresApp))
        builder.setCancelable(true)

        val editText = EditText(context)
        editText.inputType = TYPE_CLASS_TEXT
        builder.setView(editText)

        builder.setPositiveButton(getString(R.string.valid)) {
                dialog, id -> validRemoteAdd(editText.text.toString(), context)
        }
        builder.setNegativeButton(R.string.close) {
            dialog, id -> dialog.cancel()
            requireActivity().finish()
        }

        val alert = builder.create()
        alert.setTitle(getString(R.string.remoteApp))
        alert.show()
    }

    private fun validRemoteAdd(str: String, context: Context) {
        Log.d("adresse----->", str)

        val queue = Volley.newRequestQueue(context)
        val stringRequest = StringRequest(
            Request.Method.GET, str,
            { response ->
                val sharedPref = SharedPref("accessApp", requireActivity())
                sharedPref.setAppUrl(str)
                Log.d("logged---->", "yes")
                progressBar.visibility = View.GONE
                findLog()
            },
            {
                VolleyLog.d("Error---->: " + it.message);
                Toast.makeText(context, getString(R.string.unknownAddress), Toast.LENGTH_LONG).show()
                alertDialogAccessApp(context)

                val sharedPref = SharedPref("accessApp", requireActivity())
                sharedPref.clearNode()
            }
        )
        queue.add(stringRequest)
    }

    private fun findLog() {
        progressBar.visibility = View.VISIBLE
        val sharedPref = SharedPref("loginUser", requireActivity())
        val loginUser = sharedPref.getLoginUser()

        if((loginUser["email"] != null) && (loginUser["pwd"] != null)) { connectApp(loginUser) }
        else { gotoFragment("connection") }
    }

    private fun connectApp(login: MutableMap<String, String>) {
        Log.d("connectApp----->", "on essaye")
        LoginUser(requireContext()) {
            Log.d("connect ----->", it.toString());

            if(!it) { gotoFragment("connection") }
            else { gotoFragment("meeting") }
        }
    }

    private fun gotoFragment(type : String) {
        object : CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {

                val frag = when(type) {
                    "connection" -> ConnectFragment.newInstance()
                    "meeting" -> MeetingFragment.newInstance()
                    else -> ConnectFragment.newInstance()
                }

                requireActivity().supportFragmentManager.beginTransaction()
                    .setCustomAnimations(
                        android.R.anim.slide_in_left,
                        android.R.anim.fade_out,
                        android.R.anim.fade_in,
                        android.R.anim.slide_out_right
                    )
                    .addToBackStack(null)
                    .replace(R.id.container_fragment, frag)
                    .commit()
            }
        }.start()
    }

    companion object {
        fun newInstance() = WelcomeFragment()
    }
}