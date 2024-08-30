package coder.behzod.presentation.broadcastReceiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import coder.behzod.data.local.sharedPreferences.SharedPreferenceInstance
import coder.behzod.domain.useCase.notesUseCases.NotesUseCases
import coder.behzod.presentation.notifications.NotificationScheduler
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class NotificationReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationManager: NotificationManagerCompat

    @Inject
    lateinit var sharedPrefs: SharedPreferenceInstance

    @Inject
    lateinit var useCases: NotesUseCases

    @SuppressLint("NewApi", "SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onReceive(context: Context, intent: Intent?) {

        val notificationScheduler = NotificationScheduler(notificationManager)
        val title = sharedPrefs.sharedPreferences.getString("title", "")
        val content = sharedPrefs.sharedPreferences.getString("content", "")
        val requestCode = sharedPrefs.sharedPreferences.getInt("requestCode", 0)
        val stopCode = sharedPrefs.sharedPreferences.getInt("stopCode", 0)

        notificationScheduler.showNotification(
            ctx = context,
            title = title?:"",
            content = content?:"",
            requestCode = requestCode,
            stopCode = stopCode
        )
    }
}