package be.stefan.accessnfc.models

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class UpcomingMeetingItem(access: String, begin: String, end: String, id_meeting: String, prepared: String, room: String, title: String) {

    val access: String = access
    val begin: LocalDate = convertDate(begin)
    val end: LocalDate = convertDate(end)
    val id_meeting: Int = id_meeting.toInt()
    val prepared: Boolean = convertBoolean(prepared)
    val room: String = room.replaceFirstChar { if (it.isLowerCase()) it.titlecase(java.util.Locale.getDefault()) else it.toString() }
    val title: String = title.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

    private fun convertDate(s : String) : LocalDate {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDate.parse(s, formatter);
    }

    private fun convertBoolean(s: String) : Boolean {
        var b = false
        if(s.lowercase() == "true") { b = true }
        return b
    }

}