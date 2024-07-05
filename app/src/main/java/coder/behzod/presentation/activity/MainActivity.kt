package coder.behzod.presentation.activity

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import coder.behzod.data.workManager.workers.UpdateDayWorker
import coder.behzod.presentation.navigation.NavGraph
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject lateinit var notificationManager:NotificationManagerCompat
    @Inject lateinit var alarmManager: AlarmManager
    private lateinit var workManager: WorkManager

    @SuppressLint("RestrictedApi")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        workManager = WorkManager.getInstance(applicationContext)

        setContent {
            NavGraph(notificationManager, alarmManager)
            val updateDayRequest = PeriodicWorkRequestBuilder<UpdateDayWorker>(
                1, TimeUnit.DAYS
            ).build()

            workManager.enqueueUniquePeriodicWork(
                "My Worker",
                ExistingPeriodicWorkPolicy.UPDATE,
                updateDayRequest
            )
            Log.d("worker", "WorkManager: ${WorkManager.isInitialized()} ")
        }
    }
}