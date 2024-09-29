package coder.behzod.data.workManager.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import coder.behzod.domain.model.TrashModel
import coder.behzod.domain.useCase.trashUseCases.TrashUseCases
import com.google.firebase.functions.dagger.assisted.Assisted
import com.google.firebase.functions.dagger.assisted.AssistedInject


@HiltWorker
class UpdateDayWorker @AssistedInject constructor(
    @Assisted private val ctx: Context,
    @Assisted parameters: WorkerParameters,
    @Assisted private val useCases: TrashUseCases,
) : CoroutineWorker(ctx, parameters) {

    override suspend fun doWork(): Result {

        var notes: List<TrashModel>

        useCases.getListOfNotes.invoke().also {
            notes = it
        }

        var resultStatus = false

        val incrementDay = 30
        for (note in notes) {
            val model = TrashModel(
                id = note.id,
                title = note.title,
                content = note.content,
                color = note.color,
                daysLeft = note.daysLeft
            )
            if (model.daysLeft <= incrementDay ){
                useCases.updateDayUseCase(model.id!!,model.daysLeft.minus(1))
            }
            return if (model.daysLeft <= 0){
                resultStatus = true
                Result.success()
            }else{
                resultStatus = false
                Result.retry()
            }
        }
        return if (resultStatus) Result.success() else Result.retry()
    }
}