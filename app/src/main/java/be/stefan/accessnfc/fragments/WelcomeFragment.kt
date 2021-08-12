package be.stefan.accessnfc.fragments

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import be.stefan.accessnfc.R
import be.stefan.accessnfc.models.SharedPref

class WelcomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        var v : View = inflater.inflate(R.layout.fragment_welcome, container, false)
        checkNetworkAvailable()
        return v
    }

    private fun checkNetworkAvailable() {
        val connect = isNetworkAvailable()
        Log.i("internet ou pas ----->", connect.toString())
        if(!connect) { alertDialogNetwork(requireContext()) }
        else { findLog(); }
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

     private fun findLog() {
        val sharedPref = SharedPref(requireActivity())
        val loginUser = sharedPref.getLoginUser()
        if((loginUser["email"] != null) && (loginUser["pwd"] != null)) { connectApp(loginUser) }
        else { gotoConnectFragment() }
    }

    private fun connectApp(login: MutableMap<String, String>) {
        Log.d("connectApp----->", "on essaye")
    }

    private fun gotoConnectFragment() {
        object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                val addFragment = ConnectFragment.newInstance()
                requireActivity().supportFragmentManager.beginTransaction()
                    .setCustomAnimations(
                        android.R.anim.slide_in_left,
                        android.R.anim.fade_out,
                        android.R.anim.fade_in,
                        android.R.anim.slide_out_right
                    )
                    .addToBackStack(null)
                    .replace(R.id.container_fragment, addFragment)
                    .commit()
            }
        }.start()
    }

    companion object {
        fun newInstance() = WelcomeFragment()
    }
}