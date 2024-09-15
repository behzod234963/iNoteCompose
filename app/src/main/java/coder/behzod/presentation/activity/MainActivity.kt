package coder.behzod.presentation.activity

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.app.ActivityCompat
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import coder.behzod.data.local.dataStore.DataStoreInstance
import coder.behzod.data.local.sharedPreferences.SharedPreferenceInstance
import coder.behzod.data.workManager.workers.UpdateDayWorker
import coder.behzod.domain.model.NotesModel
import coder.behzod.domain.useCase.notesUseCases.NotesUseCases
import coder.behzod.presentation.broadcastReceiver.NotificationReceiver
import coder.behzod.presentation.navigation.NavGraph
import coder.behzod.presentation.notifications.NotificationTrigger
import coder.behzod.presentation.utils.constants.notesModel
import coder.behzod.presentation.viewModels.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var workManager: WorkManager
    private lateinit var alarmManager: AlarmManager
    private lateinit var notificationTrigger: NotificationTrigger
    private val viewModel : MainActivityViewModel by viewModels()

    @Inject
    lateinit var dataStoreInstance: DataStoreInstance

    @Inject
    lateinit var sharedPrefs: SharedPreferenceInstance

    @Inject
    lateinit var useCases: NotesUseCases

    @SuppressLint("RestrictedApi", "CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InitValue()
            CancelFiredAlarms()
            requestPermission()
            initUpdateDayWorkManager()
            InitAlarmManager()
            NavGraph()
        }
    }

    @SuppressLint("MutableCollectionMutableState")
    @Composable
    private fun CancelFiredAlarms() {

        val notes = viewModel.notes.value
        if (notes.isNotEmpty()) {
            notes.forEach { model ->
                if (model.isFired && !model.isRepeat) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        alarmManager.cancelAll()
                    } else {
                        val notificationReceiver =
                            Intent(this@MainActivity, NotificationReceiver::class.java)
                        val pendingIntent = PendingIntent.getBroadcast(
                            this@MainActivity,
                            model.requestCode,
                            notificationReceiver,
                            PendingIntent.FLAG_IMMUTABLE
                        )
                        alarmManager.cancel(pendingIntent)
                    }
                }
            }
        }
    }

    @SuppressLint("CoroutineCreationDuringComposition")
    @Composable
    private fun InitAlarmManager() {

        val id = dataStoreInstance.getModelId().collectAsState(initial = -1)
        val model = viewModel.model.value
        val notes = ArrayList<NotesModel>()

        if (id.value != -1) {
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.getNoteById(id.value)
            }
        }
        if (model?.alarmStatus == true) {
            notes.add(model)
            notificationTrigger.scheduleNotification(this@MainActivity, notes)
        }
    }

    @Composable
    private fun InitValue() {
        notificationTrigger = NotificationTrigger()
        alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        workManager = WorkManager.getInstance(applicationContext)
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

    private fun initUpdateDayWorkManager() {

        val updateDayRequest = PeriodicWorkRequestBuilder<UpdateDayWorker>(1, TimeUnit.DAYS).build()
        workManager.enqueueUniquePeriodicWork(
            "Update day Worker",
            ExistingPeriodicWorkPolicy.UPDATE,
            updateDayRequest
        )
    }
}