package coder.behzod.presentation.notifications

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import coder.behzod.presentation.broadcastReceiver.NotificationReceiver

class NotificationTrigger(ctx: Context) {

    private val alarmManager = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    @SuppressLint("SuspiciousIndentation", "ScheduleExactAlarm")
    fun scheduleNotification(ctx: Context,id:Int, triggerTime: Long, requestCode: Int) {

        val alarmIntent = Intent(ctx, NotificationReceiver::class.java)
        val flag = PendingIntent.FLAG_MUTABLE
        val pendingIntent = PendingIntent.getBroadcast(ctx, id, alarmIntent, flag)

        alarmIntent.putExtra("id",id)
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
    }
}