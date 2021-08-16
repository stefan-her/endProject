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

class MeetingFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var v : View = inflater.inflate(R.layout.fragment_meeting, container, false)
        return v
    }

    companion object {
        fun newInstance() = MeetingFragment()
    }
}