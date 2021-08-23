package be.stefan.accessnfc.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import be.stefan.accessnfc.R
import be.stefan.accessnfc.models.UpcomingMeetingItem

class Meetinglist(private val list: List<UpcomingMeetingItem>) : RecyclerView.Adapter<Meetinglist.ViewHolder>() {

    class ViewHolder(v : View) : RecyclerView.ViewHolder(v) {
        val tvTitle = v.findViewById(R.id.tv_title) as TextView
        val tvRoom = v.findViewById(R.id.tv_room) as TextView
        val tvBegin = v.findViewById(R.id.tv_begin) as TextView
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recycleview_meetinglist, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item : UpcomingMeetingItem = list[position]
        holder.tvTitle.text = item.title
        holder.tvRoom.text = item.room
        holder.tvBegin.text = item.begin.toString()
    }

    override fun getItemCount(): Int = list.size
}

