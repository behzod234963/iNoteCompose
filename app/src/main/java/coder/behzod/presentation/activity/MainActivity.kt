package coder.behzod.presentation.activity

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import coder.behzod.data.workManager.workers.UpdateDayWorker
import coder.behzod.presentation.navigation.NavGraph
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var workManager: WorkManager

    @SuppressLint("RestrictedApi")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        workManager = WorkManager.getInstance(applicationContext)

        setContent {
            NavGraph()
            val updateDayRequest = PeriodicWorkRequestBuilder<UpdateDayWorker>(
                1, TimeUnit.DAYS
            ).build()

            workManager.enqueueUniquePeriodicWork(
                "updateDayUniqueWorker",
                ExistingPeriodicWorkPolicy.UPDATE,
                updateDayRequest
            )
            Log.d("worker", "WorkManager: ${WorkManager.isInitialized()} ")
        }
    }
}