package coder.behzod.data.workManager.workers

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import coder.behzod.domain.model.TrashModel
import coder.behzod.domain.useCase.trashUseCases.TrashUseCases
import com.google.firebase.functions.dagger.assisted.Assisted
import com.google.firebase.functions.dagger.assisted.AssistedInject
import java.security.Permissions


@HiltWorker
class UpdateDayWorker @AssistedInject constructor(
    @Assisted private val ctx: Context,
    @Assisted parameters: WorkerParameters,
    @Assisted private val useCases: TrashUseCases,
) : CoroutineWorker(ctx, parameters) {

    override suspend fun doWork(): Result {

        Log.d("worker", "doWork: doWork is working ")

        var notes: List<TrashModel>

        useCases.getListOfNotes.invoke().also {
            notes = it
        }

        var incrementDay = 0
        var modelValue = 0

        Log.d("worker", "doWork: ${notes.size}")
        for (note in notes) {
            val model = TrashModel(
                id = note.id,
                title = note.title,
                content = note.content,
                color = note.color,
                daysLeft = note.daysLeft
            )
            val increment = model.daysLeft--
            incrementDay = increment
            Log.d("worker", "doWork: incrementDay value $incrementDay ")
            modelValue = model.daysLeft
            Log.d("worker", "doWork: modelValue value $modelValue ")

            Log.d("worker", "doWork: increment value $increment ")

            model.id?.let { useCases.updateDayUseCase(it, modelValue) }.also {
                Log.d("increment", "doWork: note was updated $note")
            }
        }
        return if (incrementDay >= modelValue){
            Result.retry()
        }else{
            Result.success()
        }
    }
}