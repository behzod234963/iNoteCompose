package coder.behzod.presentation.broadcastReceiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.util.Log
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

    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onReceive(context: Context, intent: Intent?) {

        var id = 0
        var title = ""
        var content = ""
        var modelID = 0
        val currentDayAlarmStatus = sharedPrefs.sharedPreferences.getBoolean("KEY_CURRENT_DAY_ALARM_STATUS",false)
        val workerAlarmStatus = sharedPrefs.sharedPreferences.getBoolean("KEY_WORKER_ALARM_STATUS",false)
        val workerModelId = sharedPrefs.sharedPreferences.getInt("WORKER_MODEL_ID",-1)
        val modelId = sharedPrefs.sharedPreferences.getInt("MODEL_ID",-1)

        if (currentDayAlarmStatus){
            id = modelId
        }else if (workerAlarmStatus){
            id = workerModelId
        }

        CoroutineScope(Dispatchers.IO).launch {
            if (id != -1) {
                useCases.getNoteUseCase.invoke(id).let {
                    modelID = it.id!!
                    title = it.title
                    content = it.content
                }
            }
        }
        val notificationScheduler = NotificationScheduler(notificationManager)
        notificationScheduler.showNotification(
            ctx = context,
            notificationId = modelID,
            title = title,
            content = content,
            contentRequestCode = modelID,
            stopRequestCode = modelID
        )

    }
}