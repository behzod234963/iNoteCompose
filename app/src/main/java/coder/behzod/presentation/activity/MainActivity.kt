package coder.behzod.presentation.activity

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import coder.behzod.data.local.dataStore.DataStoreInstance
import coder.behzod.data.local.sharedPreferences.SharedPreferenceInstance
import coder.behzod.data.workManager.workers.CheckDateWorker
import coder.behzod.data.workManager.workers.UpdateDayWorker
import coder.behzod.domain.model.NotesModel
import coder.behzod.domain.useCase.notesUseCases.NotesUseCases
import coder.behzod.presentation.navigation.NavGraph
import coder.behzod.presentation.notifications.NotificationTrigger
import coder.behzod.presentation.utils.constants.noteModel
import coder.behzod.presentation.viewModels.NewNoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var workManager: WorkManager
    private lateinit var alarmManager: AlarmManager
    private lateinit var notificationTrigger: NotificationTrigger
    private lateinit var newNoteViewModel: NewNoteViewModel
    @Inject lateinit var dataStoreInstance: DataStoreInstance
    @Inject lateinit var sharedPrefs: SharedPreferenceInstance
    @Inject lateinit var useCases: NotesUseCases

    @SuppressLint("RestrictedApi", "CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            InitValue()
            requestPermission()
            initCheckDateWorker()
            initUpdateDayWorkManager()
            initAlarmManager()
            NavGraph()
        }
    }

    private fun initAlarmManager() {
        val modelId = sharedPrefs.sharedPreferences.getInt("MODEL_ID",-1)
        var model = noteModel
        val workerAlarmStatus = sharedPrefs.sharedPreferences.getBoolean("ALARM_STATUS",false)
        CoroutineScope(Dispatchers.IO).launch {
            useCases.getNoteUseCase.invoke(modelId).let {
                model = it
            }
            if (workerAlarmStatus){
                notificationTrigger.scheduleNotification(this@MainActivity,model.triggerTime,modelId)
            }
        }
    }

    @Composable
    private fun InitValue() {
        notificationTrigger = NotificationTrigger(this@MainActivity)
        alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        workManager = WorkManager.getInstance(applicationContext)
        newNoteViewModel = hiltViewModel()
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }
    }

    private fun initCheckDateWorker() {

        val checkDateRequest = PeriodicWorkRequestBuilder<CheckDateWorker>(1, TimeUnit.DAYS).build()
        workManager.enqueueUniquePeriodicWork(
            "Check date Worker",
            ExistingPeriodicWorkPolicy.UPDATE,
            checkDateRequest
        )
    }

    private fun initUpdateDayWorkManager() {

        val updateDayRequest = PeriodicWorkRequestBuilder<UpdateDayWorker>(1, TimeUnit.DAYS).build()
        workManager.enqueueUniquePeriodicWork(
            "Update day Worker",
            ExistingPeriodicWorkPolicy.UPDATE,
            updateDayRequest
        )
    }
}