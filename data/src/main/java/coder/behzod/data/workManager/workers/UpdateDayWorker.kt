package coder.behzod.data.workManager.workers

import android.content.Context
import android.widget.Toast
import androidx.work.Worker
import androidx.work.WorkerParameters
import coder.behzod.data.local.sharedPreferences.SharedPreferenceInstance
import coder.behzod.domain.model.TrashModel
import coder.behzod.domain.useCase.trashUseCases.TrashUseCases
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UpdateDayWorker(
    private val ctx: Context,
    parameters: WorkerParameters,
    private val trashUseCases: TrashUseCases
) : Worker(ctx, parameters) {

    val model = ArrayList<TrashModel>()
    val sharedPrefs = SharedPreferenceInstance(ctx)

    override fun doWork(): Result {

        CoroutineScope(Dispatchers.IO).launch {
            for (model in model) {
                val incrementDay = model.daysLeft--
                model.id?.let { trashUseCases.updateDayUseCase(it, incrementDay) }

                val updateDayNotification = UpdateDayNotification(ctx)
                updateDayNotification.showNotification(
                    title = model.title,
                    content = "days left ${model.daysLeft}\n$incrementDay"
                )
            }
        }
        return Result.success()
    }
}