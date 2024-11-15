package coder.behzod.data.workManager.workers

import android.annotation.SuppressLint
import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import coder.behzod.data.local.dataStore.DataStoreInstance
import coder.behzod.data.local.sharedPreferences.SharedPreferenceInstance
import coder.behzod.domain.model.NotesModel
import coder.behzod.domain.useCase.notesUseCases.NotesUseCases
import com.google.firebase.functions.dagger.assisted.Assisted
import com.google.firebase.functions.dagger.assisted.AssistedInject
import java.time.LocalDate

@HiltWorker
class CheckDateWorker @AssistedInject constructor(
    @Assisted val ctx: Context,
    @Assisted val workerParameters: WorkerParameters,
    @Assisted val useCase: NotesUseCases
) : CoroutineWorker(ctx, workerParameters) {

    @SuppressLint("ScheduleExactAlarm")
    override suspend fun doWork(): Result {

        val dataStore = DataStoreInstance(ctx)
        val notes: List<NotesModel>
        useCase.getAllNotesUseCase.execute().also {
            notes = it
        }

        var year = 0
        var month = 0
        var day = 0

        val localYear = SharedPreferenceInstance(ctx).sharedPreferences.getInt("KEY_LOCAL_YEAR", 1)
        val localMonth = SharedPreferenceInstance(ctx).sharedPreferences.getInt("KEY_LOCAL_MONTH", 1)
        val localDay = SharedPreferenceInstance(ctx).sharedPreferences.getInt("KEY_LOCAL_DAY", 1)

        for (note in notes) {

            val model = NotesModel(
                id = note.id,
                title = note.title,
                content = note.content,
                color = note.color,
                dataAdded = note.dataAdded,
                alarmMapper = note.alarmMapper,
                alarmStatus = note.alarmStatus,
                alarmDate = note.alarmDate,
                alarmTime = note.alarmTime,
                triggerDate = note.triggerDate,
                triggerTime = note.triggerTime,
                isRepeat = note.isRepeat
            )

            if (model.alarmStatus) {
                year = model.triggerDate.minus(localMonth).minus(localDay)
                month = model.triggerDate.minus(localYear).minus(localDay)
                day = model.triggerDate.minus(localYear).minus(localMonth)

                if (year == LocalDate.now().year && month == LocalDate.now().month.value && day == LocalDate.now().dayOfMonth) {
                    dataStore.saveModelId(model.id)
                }else{
                    Result.retry()
                }
            }
        }
        return Result.success()
    }
}