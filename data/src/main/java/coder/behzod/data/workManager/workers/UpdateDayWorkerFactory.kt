package coder.behzod.data.workManager.workers

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import coder.behzod.data.local.room.TrashDao
import coder.behzod.domain.useCase.trashUseCases.TrashUseCases
import javax.inject.Inject

class UpdateDayWorkerFactory @Inject constructor(private val useCases: TrashUseCases):WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker = UpdateDayWorker(ctx = appContext, workerParameters,useCases)
}