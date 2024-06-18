package coder.behzod.presentation.utils.helpers

import android.annotation.SuppressLint
import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.icu.util.LocaleData
import android.util.Log
import androidx.core.text.util.LocalePreferences.FirstDayOfWeek.Days
import coder.behzod.data.local.sharedPreferences.SharedPreferenceInstance
import coder.behzod.presentation.utils.constants.KEY_INT
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.util.Calendar
import java.util.Date
import java.util.TimeZone
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

class DayLeft(private var daysLeft: Int = 30,private val ctx:Context):IntentService("DaysLeft") {

    @Deprecated("Deprecated in Java", ReplaceWith("TODO(\"Not yet implemented\")"))
    override fun onHandleIntent(intent: Intent?) {
        val sharedPrefs = SharedPreferenceInstance(ctx = ctx)
        daysLeft--
        sharedPrefs.sharedPreferences.edit().putInt(KEY_INT,daysLeft).apply()

        if (daysLeft == 0) daysLeft = 30
    }
}