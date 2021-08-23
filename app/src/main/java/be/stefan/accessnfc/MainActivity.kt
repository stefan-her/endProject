package be.stefan.accessnfc

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import be.stefan.accessnfc.fragments.ConnectFragment
import be.stefan.accessnfc.fragments.MeetingFragment

import be.stefan.accessnfc.fragments.WelcomeFragment
import be.stefan.accessnfc.models.SharedPref
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    lateinit var drawerLayout : DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawerMenu()
        init()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) { drawerLayout.closeDrawer(GravityCompat.START) }
        else { super.onBackPressed() }
    }

    private fun drawerMenu() {
        setSupportActionBar(findViewById(R.id.toolbar))
        drawerLayout = findViewById(R.id.drawer_layout)

        val navigationView : NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_logout -> {
                    logout()
                    true }
                else -> false
            }
        }

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            findViewById(R.id.toolbar),
            R.string.openDrawer,
            R.string.closeDrawer
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun logout() {
        val sharedPref = SharedPref("loginUser", this)
        sharedPref.clearNode()

        drawerLayout.closeDrawer(GravityCompat.START)

        val addFragment = ConnectFragment.newInstance()
        this.supportFragmentManager.beginTransaction()
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

    private fun init() {
        val welcomeFragment = WelcomeFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .add(R.id.container_fragment, welcomeFragment)
            .commit()
    }
}


