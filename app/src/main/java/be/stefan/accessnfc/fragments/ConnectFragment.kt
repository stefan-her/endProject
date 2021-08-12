package be.stefan.accessnfc.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import be.stefan.accessnfc.R
import be.stefan.accessnfc.models.SharedPref

class ConnectFragment : Fragment() {

    lateinit var et_email : EditText
    lateinit var et_pwd : EditText
    lateinit var bt_connect : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var v : View = inflater.inflate(R.layout.fragment_connect, container, false)

        et_email = v.findViewById(R.id.et_email)
        et_pwd = v.findViewById(R.id.et_pwd)
        bt_connect = v.findViewById(R.id.bt_connection)
        bt_connect.setOnClickListener { addToSharedPreferences(); }

        return v
    }

    private fun addToSharedPreferences() {
        val els = mapOf("email" to et_email.text.toString(), "pwd" to et_pwd.text.toString())
        activity?.let { SharedPref(it.applicationContext) }?.setLoginUser(els)
    }


    companion object {
        fun newInstance() = ConnectFragment()
    }
}