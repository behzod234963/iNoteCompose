package coder.behzod.presentation.activity

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import coder.behzod.data.local.dataStore.DataStoreInstance
import coder.behzod.data.local.sharedPreferences.SharedPreferenceInstance
import coder.behzod.data.workManager.workers.UpdateDayWorker
import coder.behzod.presentation.broadcastReceiver.NotificationReceiver
import coder.behzod.presentation.navigation.NavGraph
import coder.behzod.presentation.utils.constants.KEY_ALARM_STATUS
import coder.behzod.presentation.utils.constants.KEY_DAY
import coder.behzod.presentation.utils.constants.KEY_MONTH
import coder.behzod.presentation.utils.constants.KEY_TRIGGER
import coder.behzod.presentation.utils.constants.KEY_YEAR
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var notificationManager: NotificationManagerCompat

    private lateinit var workManager: WorkManager

    private lateinit var alarmManager: AlarmManager

    @Inject
    lateinit var dataStoreInstance: DataStoreInstance

    @Inject
    lateinit var sharedPrefs: SharedPreferenceInstance

    @SuppressLint("RestrictedApi", "CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            initValue()
            requestPermission()
            NavGraph()
            initWorkManager()
            InitAlarmManager()
        }
    }

    private fun initValue() {
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

    @Composable
    private fun InitAlarmManager() {
        val status = dataStoreInstance.getStatus(KEY_ALARM_STATUS).collectAsState(initial = false)

        Log.d("AlarmTrack", "onCreateStatus: ${status.value}")
        if (status.value) {
            scheduleNotification(this@MainActivity)
        }
    }

    private fun initWorkManager() {
        val updateDayRequest = PeriodicWorkRequestBuilder<UpdateDayWorker>(
            1, TimeUnit.DAYS
        ).build()

        workManager.enqueueUniquePeriodicWork(
            "My Worker",
            ExistingPeriodicWorkPolicy.UPDATE,
            updateDayRequest
        )
    }

    @SuppressLint("SuspiciousIndentation", "ScheduleExactAlarm")
    private fun scheduleNotification(ctx: Context) {

        val triggerAtMillis =
            sharedPrefs.sharedPreferences.getLong(KEY_TRIGGER, 0)
        Log.d("AlarmTrack", "NotificationScheduler: function scheduleNotification is started")
        val flag =
            PendingIntent.FLAG_IMMUTABLE
        val alarmIntent = Intent(ctx, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            ctx, 0, alarmIntent,
            flag
        )
        Log.d("AlarmTrack", "System.CurrentTimeInMillis: ${System.currentTimeMillis()}")
        Log.d("AlarmTrack", "TriggerAtMillis: $triggerAtMillis")
        Log.d("AlarmTrack", "Trigger: ${System.currentTimeMillis() + triggerAtMillis}")
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP, triggerAtMillis,
            pendingIntent
        )
    }
}