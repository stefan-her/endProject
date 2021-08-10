package be.stefan.accessnfc

import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.BoringLayout
import android.util.Log
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

import be.stefan.accessnfc.fragments.ConnectFragment
import be.stefan.accessnfc.fragments.WelcomeFragment


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()

        val connect = isNetworkAvailable()
        Log.i("internet ou pas ----->", connect.toString());
        if(!connect) { alertDialogNetwork() }
    }

    private fun alertDialogNetwork() {
        val builder : AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage(R.string.connectionlost)
        builder.setCancelable(true)
        builder.setPositiveButton(R.string.activeConnection) {
            dialog, id -> activeNetwork()
        }
        builder.setNegativeButton(R.string.close) {
            dialog, id -> dialog.cancel()
            this.finish()
        }
        val alert = builder.create()
        alert.setTitle(R.string.connection)
        alert.show()
    }

    private fun activeNetwork() {
        Toast.makeText(applicationContext, "on ouvre", Toast.LENGTH_LONG).show()
    }


    private fun isNetworkAvailable() : Boolean {
        var result = false
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if(cm is ConnectivityManager) {
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
        }
        return result
    }

    private fun init() {
        val welcomeFragment = WelcomeFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .add(R.id.container_fragment, welcomeFragment)
            .commit()
    }

    private fun gotoConnectFragment() {
        object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {

                val listFragment = ConnectFragment.newInstance()
                supportFragmentManager.beginTransaction()
                    .setCustomAnimations(
                        android.R.anim.slide_in_left,
                        android.R.anim.fade_out,
                        android.R.anim.fade_in,
                        android.R.anim.slide_out_right
                    )
                    .replace(R.id.container_fragment, listFragment)
                    .commit()
            }
        }.start()
    }
}
