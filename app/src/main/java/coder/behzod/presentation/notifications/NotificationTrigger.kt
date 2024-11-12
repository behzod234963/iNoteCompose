package coder.behzod.presentation.notifications

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import coder.behzod.domain.model.NotesModel
import coder.behzod.presentation.broadcastReceiver.NotificationReceiver

class NotificationTrigger {

    @SuppressLint("SuspiciousIndentation", "ScheduleExactAlarm", "ServiceCast")
    fun scheduleNotification(ctx: Context,model: NotesModel) {

        val alarmIntent = Intent(ctx, NotificationReceiver::class.java).let {
            it.putExtra("modeId", model.id)
            it.putExtra("title", model.title)
            it.putExtra("content", model.content)
            it.putExtra("requestCode", model.id)
        }
        val flag = PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        val pendingIntent = PendingIntent.getBroadcast(
            ctx,
            model.id,
            alarmIntent,
            flag
        )

        val alarmManager = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            model.triggerTime,
            pendingIntent
        )
    }

    fun scheduleRepeatingNotification(ctx: Context, model: NotesModel) {
        val alarmIntent = Intent(ctx, NotificationReceiver::class.java).let {
            it.putExtra("title", model.title)
            it.putExtra("content", model.content)
            it.putExtra("requestCode", model.id)
            it.putExtra("repeat", model.isRepeat)
        }
        val flag = PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        val pendingIntent =
            PendingIntent.getBroadcast(ctx, model.id, alarmIntent, flag)

        Log.d("REPEATING ALARM TEST", "scheduleRepeatingNotification: IS WORKING NOW !!!!!!!!")
        val alarmManager = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(
            AlarmManager.RTC,
            model.triggerTime,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }
}