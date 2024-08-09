package coder.behzod.data.workManager.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
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

    override suspend fun doWork(): Result {

        val notes: List<NotesModel>
        useCase.getAllNotesUseCase.execute().also {
            notes = it
        }

        val year = SharedPreferenceInstance(ctx).sharedPreferences.getInt("KEY_LOCAL_YEAR",1)
        Log.d("AlarmFix", "CheckDateYear: $year")

        val month = SharedPreferenceInstance(ctx).sharedPreferences.getInt("KEY_LOCAL_MONTH",1)
        Log.d("AlarmFix", "CheckDateMonth: $month")

        val day = SharedPreferenceInstance(ctx).sharedPreferences.getInt("KEY_LOCAL_DAY",1)
        Log.d("AlarmFix", "CheckDateDay: $day")

        var localYear = 0
        var localMonth = 0
        var localDay = 0
        var localDate = LocalDate.now()

        for (note in notes) {

            val model = NotesModel(
                id = note.id,
                title = note.title,
                content = note.content,
                color = note.color,
                dataAdded = note.dataAdded,
                alarmStatus = note.alarmStatus,
                triggerDate = note.triggerDate,
                triggerTime = note.triggerTime
            )

            if (model.alarmStatus) {

                Log.d("AlarmFix", "CheckDateModel: $model")
                if (year != 0 && month != 0 && day != 0) {

                    localYear = model.triggerDate.minus(month).minus(day)
                    Log.d("AlarmFix", "CheckDateLocalYear: $localYear")

                    localMonth = model.triggerDate.minus(year).minus(day)
                    Log.d("AlarmFix", "CheckDateLocalMonth: $localMonth")

                    localDay = model.triggerDate.minus(year).minus(month)
                    Log.d("AlarmFix", "CheckDateLocalDay: $localDay")

                    localDate = LocalDate.of(localYear,localMonth,localDay)
                    Log.d("AlarmFix", "CheckDateLocalDate: $localDate")

                    if (localDate == LocalDate.now()){

                        val status = true
                        SharedPreferenceInstance(ctx).sharedPreferences.edit().putInt("MODEL_ID",model.id?:0).apply()
                        Log.d("AlarmFix", "CheckDateModelId: ${model.id}")

                        SharedPreferenceInstance(ctx).sharedPreferences.edit().putBoolean("ALARM_STATUS",status).apply()
                        Log.d("AlarmFix", "CheckDateAlarmStatus: $status")
                    }
                }
            }
        }
        return Result.success()
    }
}