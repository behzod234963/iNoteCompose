package coder.behzod.presentation.notifications

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import coder.behzod.presentation.broadcastReceiver.NotificationReceiver

class NotificationTrigger(private val ctx: Context) {

    private val alarmManager = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    @SuppressLint("SuspiciousIndentation", "ScheduleExactAlarm")
    fun scheduleNotification(ctx: Context, triggerTime: Long, requestCode:Int) {

        val alarmIntent = Intent(ctx, NotificationReceiver::class.java)
        val flag = PendingIntent.FLAG_IMMUTABLE
        val pendingIntent = PendingIntent.getBroadcast(ctx, requestCode, alarmIntent, flag)

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
    }
}