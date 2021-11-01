package ir.vasl.chatkitv4core.util.helper

import java.text.SimpleDateFormat
import java.util.*

object DateTimeHelper {

    fun getDateCurrentTimeZone(timestamp: Long): String {
        try {
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            return formatter.format(Date(timestamp))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return "known time :|"
    }
}