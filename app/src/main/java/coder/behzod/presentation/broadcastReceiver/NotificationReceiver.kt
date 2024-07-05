package coder.behzod.presentation.broadcastReceiver

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import coder.behzod.data.local.sharedPreferences.SharedPreferenceInstance
import coder.behzod.presentation.notifications.NotificationScheduler
import coder.behzod.presentation.utils.constants.KEY_ALARM_CONTENT
import coder.behzod.presentation.utils.constants.KEY_ALARM_TITLE
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class NotificationReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationManager: NotificationManagerCompat

    @Inject
    lateinit var alarmManager: AlarmManager

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onReceive(context: Context?, intent: Intent?) {

        val sharedPrefs = context?.let { SharedPreferenceInstance(it) }
        val title = sharedPrefs?.sharedPreferences?.getString(KEY_ALARM_TITLE,"")
        val content = sharedPrefs?.sharedPreferences?.getString(KEY_ALARM_CONTENT,"")
        Log.d("TAG", "onCreate: NotificationReceiver is started")

        val notificationScheduler = NotificationScheduler(notificationManager, alarmManager)
        if (context != null) {
            notificationScheduler.scheduleNotification(context, 0)
        }
        if (context != null) {
            if (title != null) {
                if (content != null) {
                    notificationScheduler.showNotification(ctx = context, title = title,content)
                }
            }
        }
    }
}