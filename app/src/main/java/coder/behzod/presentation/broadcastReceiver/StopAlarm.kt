package coder.behzod.presentation.broadcastReceiver

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import coder.behzod.data.local.dataStore.DataStoreInstance
import coder.behzod.data.local.sharedPreferences.SharedPreferenceInstance
import coder.behzod.domain.useCase.notesUseCases.NotesUseCases
import coder.behzod.presentation.utils.constants.noteModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class StopAlarm : BroadcastReceiver() {
    @Inject lateinit var notificationManager: NotificationManagerCompat
    @Inject lateinit var dataStore:DataStoreInstance
    @Inject lateinit var sharedPrefs:SharedPreferenceInstance

    @SuppressLint("SuspiciousIndentation")
    override fun onReceive(context: Context, intent: Intent?) {

        var id = 0
        var model = noteModel
        val modelId = sharedPrefs.sharedPreferences.getInt("MODEL_ID",-1)

        id = modelId

        if (id != -1) {
            CoroutineScope(Dispatchers.IO).launch {
                useCases.getNoteUseCase.invoke(id).let {
                    model = it
                }
            }
        }

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, model.requestCode, alarmIntent, PendingIntent.FLAG_IMMUTABLE)

            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
            notificationManager.cancel(4)
            CoroutineScope(Dispatchers.IO).launch {
                useCases.updateStatusUseCase.execute(model.id!!,model.alarmStatus)
            }
    }
}