package coder.behzod.presentation.activity

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import coder.behzod.data.workManager.workers.UpdateDayWorker
import coder.behzod.presentation.navigation.NavGraph
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @SuppressLint("RestrictedApi")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val updateDayRequest: WorkRequest =
            PeriodicWorkRequestBuilder<UpdateDayWorker>(
                repeatInterval = 1L,
                repeatIntervalTimeUnit = TimeUnit.DAYS,
            ).build()

        WorkManager.getInstance(this@MainActivity)
            .enqueue(updateDayRequest)

        setContent {
            NavGraph()
        }
    }
}