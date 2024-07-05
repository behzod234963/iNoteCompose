package coder.behzod.presentation.broadcastReceiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StopAlarm:BroadcastReceiver() {

    @Inject lateinit var alarmManager: AlarmManager
    @Inject lateinit var notificationManager:NotificationManagerCompat

    override fun onReceive(context: Context?, intent: Intent?) {

        val alarmIntent = Intent(context,NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context,0,alarmIntent,
            PendingIntent.FLAG_IMMUTABLE)
        alarmManager.cancel(pendingIntent).run {
            notificationManager.cancel(0)
        }
        Log.d("TAG", "onCreate: Alarm is cancelled")
    }
}