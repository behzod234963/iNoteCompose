package coder.behzod.presentation.activity

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import coder.behzod.data.local.sharedPreferences.SharedPreferenceInstance
import coder.behzod.data.workManager.workers.UpdateDayWorker
import coder.behzod.presentation.navigation.NavGraph
import coder.behzod.presentation.notifications.NotificationScheduler
import coder.behzod.presentation.utils.constants.KEY_ALARM_DATE_AND_TIME
import coder.behzod.presentation.viewModels.NewNoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var notificationManager: NotificationManagerCompat
    @Inject
    lateinit var alarmManager: AlarmManager
    private lateinit var workManager: WorkManager
    @Inject
    lateinit var sharedPrefs: SharedPreferenceInstance

    @SuppressLint("RestrictedApi")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }
        workManager = WorkManager.getInstance(applicationContext)
        val alarmDateAndTime = sharedPrefs.sharedPreferences.getLong(KEY_ALARM_DATE_AND_TIME, 0L)

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
            val status = newNoteViewModel.status

            if (status.value) {
                NotificationScheduler(
                    notificationManager,
                    alarmManager
                ).scheduleNotification(this@MainActivity, newNoteViewModel.dateAndTime.value)
            }
        }
    }
}