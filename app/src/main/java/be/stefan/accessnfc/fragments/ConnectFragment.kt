package be.stefan.accessnfc.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import be.stefan.accessnfc.R
import be.stefan.accessnfc.api.LoginUser
import be.stefan.accessnfc.models.SharedPref

class ConnectFragment : Fragment() {

    lateinit var et_email : EditText
    lateinit var et_pwd : EditText
    lateinit var bt_connect : Button
    lateinit var tv_error : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var v : View = inflater.inflate(R.layout.fragment_connect, container, false)

        tv_error = v.findViewById(R.id.error)
        tv_error.visibility = View.GONE

        et_email = v.findViewById(R.id.et_email)
        et_pwd = v.findViewById(R.id.et_pwd)
        bt_connect = v.findViewById(R.id.bt_connection)
        bt_connect.setOnClickListener {
            addToSharedPreferences()
            activity?.hideKeyboard(it)
            LoginUser(requireContext()) {
                Log.d("connect ----->", it.toString());

                if(!it) { tv_error.visibility = View.VISIBLE }
                else {
                    val addFragment = MeetingFragment.newInstance()
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
            }
        }

        return v
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun addToSharedPreferences() {
        val els = mapOf("email" to et_email.text.toString(), "pwd" to et_pwd.text.toString())
        activity?.let {
            SharedPref("loginUser", it.applicationContext)
        }?.setLoginUser(els)
    }

    companion object {
        fun newInstance() = ConnectFragment()
    }
}