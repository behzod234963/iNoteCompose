package coder.behzod.presentation.notifications

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import coder.behzod.domain.model.NotesModel
import coder.behzod.presentation.broadcastReceiver.NotificationReceiver
import coder.behzod.presentation.broadcastReceiver.StopAlarm

class NotificationTrigger {

    @SuppressLint("SuspiciousIndentation", "ScheduleExactAlarm", "ServiceCast")
    fun scheduleNotification(ctx: Context, list: ArrayList<NotesModel>) {

        list.forEach { model ->
            val stopIntent = Intent(ctx, StopAlarm::class.java)
            stopIntent.putExtra("req.Code", model.requestCode)
            val alarmIntent = Intent(ctx, NotificationReceiver::class.java)
            alarmIntent.putExtra("modeId", model.id)
            alarmIntent.putExtra("title", model.title)
            alarmIntent.putExtra("content", model.content)
            alarmIntent.putExtra("requestCode", model.requestCode)
            val flag = PendingIntent.FLAG_MUTABLE
            val pendingIntent =
                PendingIntent.getBroadcast(ctx, model.requestCode, alarmIntent, flag)


            val alarmManager = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                model.triggerTime,
                pendingIntent
            )
        }
    }
}