package coder.behzod.presentation.broadcastReceiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import coder.behzod.data.local.dataStore.DataStoreInstance
import coder.behzod.domain.useCase.notesUseCases.NotesUseCases
import coder.behzod.presentation.utils.constants.KEY_ALARM_STATUS
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class StopAlarm : BroadcastReceiver() {
    @Inject lateinit var notificationManager: NotificationManagerCompat
    @Inject lateinit var dataStore:DataStoreInstance
    @Inject lateinit var useCases: NotesUseCases

    override fun onReceive(context: Context, intent: Intent?) {

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context, 0, alarmIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
        notificationManager.cancel(0)
    }
}