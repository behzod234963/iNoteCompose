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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
    override fun onReceive(context: Context, intent: Intent) {

        val notificationScheduler = NotificationScheduler(notificationManager)
        val requestCode = intent.getIntExtra("requestCode", -1)
        val title = intent.getStringExtra("title")
        val content = intent.getStringExtra("content")
        val isRepeat = intent.getBooleanExtra("repeat",false)
        val isFired = intent.getBooleanExtra("fired",false)

        notificationScheduler.showNotification(
            ctx = context,
            requestCode = requestCode,
            title = title ?: "",
            content = content ?: ""
        ).run {
            Intent(context, StopAlarm::class.java).let {
                sharedPrefs.sharedPreferences.edit().putInt("stopRequestCode", requestCode).apply()
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            useCases.updateIsFiredUseCase.execute(requestCode,true)
            useCases.updateStatusUseCase.execute(requestCode,false)
        }
    }
}