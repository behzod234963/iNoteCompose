package coder.behzod.presentation.utils.helpers

import android.annotation.SuppressLint
import android.util.Log
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class DayLeft(private var daysLeft: Int) {

    @SuppressLint("SimpleDateFormat")
    private val sdf = SimpleDateFormat("dd")
    private val date = Calendar.DAY_OF_WEEK-6
    private var result = daysLeft
    fun execute(): Int {
        Log.d("fix", "execute: $date")
        result = daysLeft-date
        return result
    }
}