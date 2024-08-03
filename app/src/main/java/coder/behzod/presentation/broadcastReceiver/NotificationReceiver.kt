package coder.behzod.presentation.broadcastReceiver

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
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
    lateinit var sharedPrefs:SharedPreferenceInstance

    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onReceive(context: Context, intent: Intent?) {

        val sharedPrefs = SharedPreferenceInstance(context)
        val title = sharedPrefs.sharedPreferences.getString(KEY_ALARM_TITLE, "")
        val content = sharedPrefs.sharedPreferences.getString(KEY_ALARM_CONTENT, "")

        val notificationScheduler = NotificationScheduler(notificationManager)

        if (title != null) {
                notificationScheduler.showNotification(ctx = context, title = title, content!!)
            }
    }
}