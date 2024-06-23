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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@HiltWorker
class UpdateDayWorker @AssistedInject constructor(
    @Assisted private val ctx: Context,
    @Assisted parameters: WorkerParameters,
    @Assisted private val useCases: TrashUseCases,
) : CoroutineWorker(ctx, parameters) {

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(
            1,notificationCreator(
                title = "Work Manager",
                content = "Updating..."
            )
        )
    }

    fun notificationCreator(title:String,content:String):Notification{

        val notificationManager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "1"

        val notification = NotificationCompat.Builder(ctx, channelId)
            .setSmallIcon(android.R.drawable.ic_delete)
            .setContentText(content)
            .setContentTitle(title)
            .build()
        notificationManager.notify(1,notification)
        return notification
    }

    override suspend fun doWork(): Result {

        Log.d("worker", "doWork: doWork is working ")

        var notes: List<TrashModel> = emptyList()

        useCases.getListOfNotes.invoke().also {
            notes = it
        }

        Log.d("worker", "doWork: ${notes.size}")
        CoroutineScope(Dispatchers.IO).launch {
            for (note in notes) {
                val model = TrashModel(
                    id = note.id,
                    title = note.title,
                    content = note.content,
                    color = note.color,
                    daysLeft = note.daysLeft
                )
                val increment = model.daysLeft - 1
                Log.d("worker", "doWork: increment value $increment ")

                model.id?.let { useCases.updateDayUseCase(it, increment) }.also {
                    Log.d("increment", "doWork: note was updated $note")
                }
            }
        }
       return Result.success()
    }
}