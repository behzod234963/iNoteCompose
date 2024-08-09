package coder.behzod.data.workManager.workers

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import coder.behzod.domain.useCase.notesUseCases.NotesUseCases
import javax.inject.Inject


class CheckDateWorkerFactory @Inject constructor( private val useCases: NotesUseCases):WorkerFactory(){
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker = CheckDateWorker( appContext,workerParameters,useCases )
}