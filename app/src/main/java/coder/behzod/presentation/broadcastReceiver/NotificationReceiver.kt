package coder.behzod.presentation.broadcastReceiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import coder.behzod.data.local.sharedPreferences.SharedPreferenceInstance
import coder.behzod.domain.model.NotesModel
import coder.behzod.domain.useCase.notesUseCases.NotesUseCases
import coder.behzod.presentation.notifications.NotificationScheduler
import coder.behzod.presentation.utils.constants.noteModel
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
    override fun onReceive(context: Context, intent: Intent?) {

        var model = noteModel
        var list = emptyList<NotesModel>()
        CoroutineScope(Dispatchers.IO).launch {
            useCases.getAllNotesUseCase.execute().let {
                list = it
            }
        }

        for (i in list) {
            model = NotesModel(
                id = i.id,
                title = i.title,
                content = i.content,
                color = i.color,
                dataAdded = i.dataAdded,
                alarmStatus = i.alarmStatus,
                requestCode = i.requestCode,
                notificationCode = i.notificationCode,
                stopCode = i.stopCode,
                triggerDate = i.triggerDate,
                triggerTime = i.triggerTime
            )
        }
        val notificationScheduler = NotificationScheduler(notificationManager)
        if (model.alarmStatus) {
            notificationScheduler.showNotification(
                ctx = context,
                title = model.title,
                content = model.content,
                requestCode = model.requestCode,
                notificationCode = model.notificationCode,
                stopCode = model.stopCode
            )
        }
    }
}