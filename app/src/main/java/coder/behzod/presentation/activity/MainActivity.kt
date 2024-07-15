package coder.behzod.presentation.activity

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.collectAsState
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import coder.behzod.data.local.dataStore.DataStoreInstance
import coder.behzod.data.local.sharedPreferences.SharedPreferenceInstance
import coder.behzod.data.workManager.workers.UpdateDayWorker
import coder.behzod.presentation.broadcastReceiver.NotificationReceiver
import coder.behzod.presentation.broadcastReceiver.StopAlarm
import coder.behzod.presentation.navigation.NavGraph
import coder.behzod.presentation.notifications.NotificationScheduler
import coder.behzod.presentation.utils.constants.KEY_ALARM_STATUS
import coder.behzod.presentation.viewModels.NewNoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var notificationManager: NotificationManagerCompat

    @Inject
    lateinit var alarmManager: AlarmManager
    private lateinit var workManager: WorkManager

    @Inject lateinit var dataStoreInstance: DataStoreInstance

    @Inject
    lateinit var sharedPrefs: SharedPreferenceInstance

    @SuppressLint("RestrictedApi", "CoroutineCreationDuringComposition")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {

            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                0
            )
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(android.Manifest.permission.SCHEDULE_EXACT_ALARM),
                0
            )
        }
        workManager = WorkManager.getInstance(applicationContext)

        setContent {
            NavGraph()
            val updateDayRequest = PeriodicWorkRequestBuilder<UpdateDayWorker>(
                1, TimeUnit.DAYS
            ).build()

            workManager.enqueueUniquePeriodicWork(
                "My Worker",
                ExistingPeriodicWorkPolicy.UPDATE,
                updateDayRequest
            )
            Log.d("worker", "WorkManager: ${WorkManager.isInitialized()} ")

            val newNoteViewModel: NewNoteViewModel = hiltViewModel()
            val status = sharedPrefs.sharedPreferences.getBoolean(KEY_ALARM_STATUS,false)

            if (status) {
                Log.d("alarm", "MainActivity: notificationScheduler is started ")
                NotificationScheduler(
                    notificationManager,
                    alarmManager
                ).scheduleNotification(this@MainActivity, newNoteViewModel.dateAndTime.value)
            }
        }
    }
}