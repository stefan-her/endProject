package be.stefan.accessnfc.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import be.stefan.accessnfc.R
import be.stefan.accessnfc.adapters.Meetinglist
import be.stefan.accessnfc.api.UpcomingMeetings
import be.stefan.accessnfc.models.UpcomingMeetingItem

class MeetingFragment : Fragment() {

    lateinit var progressBar : ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var v : View = inflater.inflate(R.layout.fragment_meeting, container, false)
        progressBar = v.findViewById(R.id.progressBar)
        UpcomingMeetings(requireContext()) {
            progressBar.visibility = View.GONE
            prepareRecycleView(v, it)
        }

        return v
    }

    private fun prepareRecycleView(v : View, list : List<UpcomingMeetingItem>) {

        val recyclerView = v.findViewById(R.id.rv_meetinglist) as RecyclerView
        recyclerView.setHasFixedSize(false)

        val dividerItemDecoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        ContextCompat.getDrawable(requireContext(), R.drawable.diviser)?.let {
            dividerItemDecoration.setDrawable(it)
        }
        recyclerView.addItemDecoration(dividerItemDecoration)

        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        val adapter = Meetinglist(list)
        recyclerView.adapter = adapter
    }

    companion object {
        fun newInstance() = MeetingFragment()
    }
}