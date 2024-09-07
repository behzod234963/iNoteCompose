package coder.behzod.presentation.broadcastReceiver

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import coder.behzod.data.local.sharedPreferences.SharedPreferenceInstance
import coder.behzod.domain.useCase.notesUseCases.NotesUseCases
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class StopAlarm : BroadcastReceiver() {
    @Inject
    lateinit var notificationManager: NotificationManagerCompat

    @Inject
    lateinit var sharedPrefs: SharedPreferenceInstance

    @Inject
    lateinit var useCases: NotesUseCases

    @SuppressLint("SuspiciousIndentation")
    override fun onReceive(context: Context, intent: Intent) {

        val requestCode = sharedPrefs.sharedPreferences.getInt("stopRequestCode", -1)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, NotificationReceiver::class.java)

        if (requestCode != -1){

            val pendingIntent = PendingIntent.getBroadcast(context,requestCode,alarmIntent,
                PendingIntent.FLAG_IMMUTABLE)
            alarmManager.cancel(pendingIntent)

            pendingIntent.cancel()

            notificationManager.cancel(requestCode)


            CoroutineScope(Dispatchers.Default).launch {
                useCases.updateStatusUseCase.execute(
                    requestCode = requestCode,
                    status = false
                )
            }
            sharedPrefs.sharedPreferences.edit().putInt("stopRequestCode",-1).apply()
        }
    }
}
