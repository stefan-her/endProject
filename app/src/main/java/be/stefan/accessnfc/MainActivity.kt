package be.stefan.accessnfc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import be.stefan.accessnfc.fragments.WelcomeFragment
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }
    private fun init() {
        val welcomeFragment = WelcomeFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .add(R.id.container_fragment, welcomeFragment)
            .commit()
    }
}
