package coder.behzod.presentation.utils.helpers

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import java.util.Calendar

@SuppressLint("ScheduleExactAlarm")
fun Int.periodicDeleting (ctx:Context){

    val alarmManager = ctx.getSystemService(ALARM_SERVICE) as AlarmManager
    val intent = Intent(ctx,DayLeft::class.java)
    val pendingIntent = PendingIntent.getService(ctx,0,intent,PendingIntent.FLAG_MUTABLE)

    val calendar = Calendar.getInstance()
    calendar.set(Calendar.DAY_OF_MONTH,1)

    alarmManager.setExact(AlarmManager.RTC,calendar.timeInMillis,pendingIntent)
}