package coder.behzod.presentation.broadcastReceiver

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import coder.behzod.data.local.dataStore.DataStoreInstance
import coder.behzod.data.local.sharedPreferences.SharedPreferenceInstance
import coder.behzod.presentation.notifications.NotificationScheduler
import coder.behzod.presentation.utils.constants.KEY_ALARM_CONTENT
import coder.behzod.presentation.utils.constants.KEY_ALARM_TITLE
import coder.behzod.presentation.utils.constants.KEY_TRIGGER
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
        val title = sharedPrefs?.sharedPreferences?.getString(KEY_ALARM_TITLE, "")
        val content = sharedPrefs?.sharedPreferences?.getString(KEY_ALARM_CONTENT, "")
        Log.d("alarm", "NotificationReceiver: NotificationReceiver is started")

        val dataStore = context?.let { DataStoreInstance(it).getTrigger(KEY_TRIGGER) }
        val notificationScheduler = NotificationScheduler(notificationManager, alarmManager)

        val trigger = sharedPrefs?.sharedPreferences?.getLong(KEY_TRIGGER,0L)

        if (trigger != null && trigger != 0L) {
            notificationScheduler.scheduleNotification(context, trigger)
        }
            if (title != null) {
                notificationScheduler.showNotification(ctx = context, title = title, content!!)
            }
    }
}