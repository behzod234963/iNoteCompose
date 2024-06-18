package coder.behzod.data.workManager.request

import android.content.Context
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import coder.behzod.data.workManager.workers.UpdateDayWorker
import java.util.concurrent.TimeUnit

class UpdateDayRequest(private val ctx: Context) {

    operator fun invoke() {

        val updateDayRequest: WorkRequest =
            PeriodicWorkRequestBuilder<UpdateDayWorker>(
                repeatInterval = 1,
                repeatIntervalTimeUnit = TimeUnit.DAYS
            )
                .build()
        WorkManager.getInstance(ctx)
            .enqueue(updateDayRequest)
    }
}