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
import coder.behzod.presentation.utils.constants.notes
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
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

        val id = intent?.getIntExtra("id",0)
        var model = noteModel
        val notificationScheduler = NotificationScheduler(notificationManager)
        CoroutineScope(Dispatchers.IO).launch {
            useCases.getNoteUseCase(id!!).let {
                model = it
            }
        }.cancel()

        if (model.alarmStatus) {
            notificationScheduler.showNotification(
                ctx = context,
                title = model.title,
                content = model.content,
                requestCode = model.requestCode,
                stopCode = model.stopCode
            )
        }
    }
}