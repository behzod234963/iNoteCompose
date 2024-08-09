package coder.behzod.presentation.activity

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import coder.behzod.data.local.dataStore.DataStoreInstance
import coder.behzod.data.local.sharedPreferences.SharedPreferenceInstance
import coder.behzod.data.workManager.workers.CheckDateWorker
import coder.behzod.data.workManager.workers.UpdateDayWorker
import coder.behzod.domain.model.NotesModel
import coder.behzod.domain.useCase.notesUseCases.NotesUseCases
import coder.behzod.presentation.broadcastReceiver.NotificationReceiver
import coder.behzod.presentation.navigation.NavGraph
import coder.behzod.presentation.utils.constants.noteModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var notificationManager: NotificationManagerCompat
    private lateinit var workManager: WorkManager
    private lateinit var alarmManager: AlarmManager

    @Inject
    lateinit var dataStoreInstance: DataStoreInstance

    @Inject
    lateinit var sharedPrefs: SharedPreferenceInstance

    @Inject
    lateinit var useCases: NotesUseCases

    @SuppressLint("RestrictedApi", "CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            initValue()
            requestPermission()
            NavGraph()
            initCheckDateWorker()
            initUpdateDayWorkManager()
            InitAlarmManager()
        }
    }

    private fun initValue() {
        alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        workManager = WorkManager.getInstance(applicationContext)
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }
    }

    @SuppressLint("CoroutineCreationDuringComposition")
    @Composable
    private fun InitAlarmManager() {

        val alarmStatus = remember { mutableStateOf(sharedPrefs.sharedPreferences.getBoolean("PICKED_ALARM_STATUS",false)) }
        if (alarmStatus.value){

            var model = noteModel
            val id = getModelId()
            var year = 0
            var month = 0
            var day = 0
            val localYear = sharedPrefs.sharedPreferences.getInt("KEY_LOCAL_YEAR", 1)
            Log.d("AlarmFix", "MainActivityLocalYear: $localYear")

            val localMonth = sharedPrefs.sharedPreferences.getInt("KEY_LOCAL_MONTH", 1)
            Log.d("AlarmFix", "MainActivityLocalMonth: $localMonth")

            val localDay = sharedPrefs.sharedPreferences.getInt("KEY_LOCAL_DAY", 1)
            Log.d("AlarmFix", "MainActivityLocalDay: $localDay")

            val status = dataStoreInstance.getStatus("ALARM_STATUS").collectAsState(initial = false)
            Log.d("AlarmFix", "MainActivityAlarmStatus: ${status.value}")

            CoroutineScope(Dispatchers.IO).launch {
                model = useCases.getNoteUseCase.invoke(id)
                Log.d("AlarmFix", "MainActivityModel: $model")
                if (id != -1) {
                    year = model.triggerDate.minus(localMonth).minus(localDay)
                    Log.d("AlarmFix", "MainActivityYear: $year")

                    month = model.triggerDate.minus(localYear).minus(localDay)
                    Log.d("AlarmFix", "MainActivityMonth: $month")

                    day = model.triggerDate.minus(localYear).minus(localMonth)
                    Log.d("AlarmFix", "MainActivityDay: $day")

                    val localDate = LocalDate.of(year, month, day)
                    Log.d("AlarmFix", "MainActivityLocalDate: $localDate")

                    if (localDate == LocalDate.now()) {
                        scheduleNotification(this@MainActivity, model.triggerTime)
                        Log.d("AlarmFix", "MainActivityTriggerTimeToday: ${model.triggerTime}")
                    }
                } else if (status.value) {

                    scheduleNotification(this@MainActivity, model.triggerTime)
                    Log.d("AlarmFix", "MainActivityTriggerTimeOnOtherDay: ${model.triggerTime}")
                }
            }
        }
    }

    private fun getModelId(): Int {
        var list: List<NotesModel> = emptyList()
        CoroutineScope(Dispatchers.IO).launch {
            useCases.getAllNotesUseCase.execute().also {
                list = it
            }
        }
        var id = -1
        if (list.isNotEmpty()) {
            for (note in list) {
                val model = NotesModel(
                    id = note.id,
                    title = note.title,
                    content = note.content,
                    color = note.color,
                    dataAdded = note.dataAdded,
                )
                if (model.alarmStatus) {
                    id = model.id!!
                } else {
                    id = -1
                }
            }
        }
        Log.d("AlarmFix", "getModelId: $id")
        return id
    }


    private fun initCheckDateWorker() {

        val checkDateRequest = PeriodicWorkRequestBuilder<CheckDateWorker>(1, TimeUnit.DAYS).build()
        workManager.enqueueUniquePeriodicWork(
            "Check date Worker",
            ExistingPeriodicWorkPolicy.UPDATE,
            checkDateRequest
        )
    }

    private fun initUpdateDayWorkManager() {

        val updateDayRequest = PeriodicWorkRequestBuilder<UpdateDayWorker>(1, TimeUnit.DAYS).build()
        workManager.enqueueUniquePeriodicWork(
            "Update day Worker",
            ExistingPeriodicWorkPolicy.UPDATE,
            updateDayRequest
        )
    }

    @SuppressLint("SuspiciousIndentation", "ScheduleExactAlarm")
    private fun scheduleNotification(ctx: Context, triggerTime: Long) {

        val alarmIntent = Intent(ctx, NotificationReceiver::class.java)
        val flag = PendingIntent.FLAG_IMMUTABLE
        val pendingIntent = PendingIntent.getBroadcast(ctx, 0, alarmIntent, flag)

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
    }
}