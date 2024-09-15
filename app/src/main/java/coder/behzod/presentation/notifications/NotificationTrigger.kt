package coder.behzod.presentation.notifications

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import coder.behzod.domain.model.NotesModel
import coder.behzod.presentation.broadcastReceiver.NotificationReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationTrigger {

    @SuppressLint("SuspiciousIndentation", "ScheduleExactAlarm", "ServiceCast")
    fun scheduleNotification(ctx: Context, list: ArrayList<NotesModel>) {

        list.forEach { model ->
            val alarmIntent = Intent(ctx, NotificationReceiver::class.java).let {
                it.putExtra("modeId", model.id)
                it.putExtra("title", model.title)
                it.putExtra("content", model.content)
                it.putExtra("requestCode", model.requestCode)
            }
            Log.d("AlarmManager.cancel()", "scheduleNotification: ${model.requestCode}")
            val flag = PendingIntent.FLAG_ONE_SHOT
            val pendingIntent = PendingIntent.getBroadcast(
                ctx,
                model.requestCode,
                alarmIntent,
                flag or PendingIntent.FLAG_IMMUTABLE
            )


            val alarmManager = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                model.triggerTime,
                pendingIntent
            )
        }
    }
    fun scheduleRepeatingNotification(ctx: Context,repeatingAlarmList: ArrayList<NotesModel>) {
        repeatingAlarmList.forEach {model->
            val alarmIntent = Intent(ctx, NotificationReceiver::class.java).let {
                it.putExtra("modeId", model.id)
                it.putExtra("title", model.title)
                it.putExtra("content", model.content)
                it.putExtra("requestCode", model.requestCode)
                it.putExtra("repeat",model.isRepeat)
            }
            val flag = PendingIntent.FLAG_MUTABLE
            val pendingIntent =
                PendingIntent.getBroadcast(ctx, model.requestCode, alarmIntent, flag)

            val alarmManager = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,model.triggerTime,model.triggerTime+86400000,pendingIntent)

        }
    }
}