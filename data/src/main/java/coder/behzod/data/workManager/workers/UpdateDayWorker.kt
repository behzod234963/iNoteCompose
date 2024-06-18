package coder.behzod.data.workManager.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import coder.behzod.data.local.sharedPreferences.SharedPreferenceInstance
import coder.behzod.domain.model.TrashModel
import coder.behzod.domain.useCase.trashUseCases.UpdateDayUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UpdateDayWorker(
    private val ctx: Context,
    private val parameters: WorkerParameters,
    private val updateDay: UpdateDayUseCase
) : Worker(ctx, parameters) {

    val model = ArrayList<TrashModel>()
    val sharedPrefs = SharedPreferenceInstance(ctx)

    override fun doWork(): Result {

        CoroutineScope(Dispatchers.IO).launch {
            for (model in model) {
                val trashModel = TrashModel(
                    id = model.id,
                    title = model.title,
                    content = model.content,
                    daysLeft = model.daysLeft,
                    color = model.color
                )
                val incrementDay = model.daysLeft--
                model.id?.let { updateDay(it,incrementDay) }
            }
        }
        return Result.success()
    }
}