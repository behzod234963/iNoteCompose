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
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.navigation.compose.hiltViewModel
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
import coder.behzod.presentation.viewModels.NewNoteViewModel
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

    private lateinit var newNoteViewModel: NewNoteViewModel

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
            InitValue()
            requestPermission()
            initCheckDateWorker()
            initUpdateDayWorkManager()
            NavGraph()
            InitAlarmManager()
        }
    }

    @Composable
    private fun InitValue() {
        alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        workManager = WorkManager.getInstance(applicationContext)
        newNoteViewModel = hiltViewModel()
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
        var modelId = -1
        modelId = newNoteViewModel.alarmId.value
        val alarmStatus = newNoteViewModel.alarmStatus.value
        var scheduledAlarmStatus = sharedPrefs.sharedPreferences.getBoolean("ALARM_STATUS",false)
        var model = noteModel
        var localYear = 0
        var localMonth = 0
        var localDay = 0
        var year = 0
        var month = 0
        var day = 0
        var triggerDate = LocalDate.now()

        if (alarmStatus){
            if (modelId == -1){
                getModelId().let {
                    modelId = it
                }
                CoroutineScope(Dispatchers.IO).launch {
                    useCases.getNoteUseCase(modelId).let {
                        model = it
                    }
                    if(model.alarmStatus){
                        localYear = newNoteViewModel.localYear.value
                        localMonth = newNoteViewModel.localMonth.value
                        localDay = newNoteViewModel.localDay.value

                        year = model.triggerDate.minus(localMonth).minus(localDay)
                        month = model.triggerDate.minus(localYear).minus(localDay)
                        day = model.triggerDate.minus(localYear).minus(localMonth)

                        triggerDate = LocalDate.of(year,month,day)

                        if (triggerDate == LocalDate.now()){
                            scheduleNotification(this@MainActivity, model.triggerTime,model.id!!)
                        }
                    }
                }
            }else{
                CoroutineScope(Dispatchers.IO).launch {
                    useCases.getNoteUseCase(modelId).let {
                        model = it
                    }
                    if(model.alarmStatus){
                        localYear = newNoteViewModel.localYear.value
                        localMonth = newNoteViewModel.localMonth.value
                        localDay = newNoteViewModel.localDay.value

                        year = model.triggerDate.minus(localMonth).minus(localDay)
                        month = model.triggerDate.minus(localYear).minus(localDay)
                        day = model.triggerDate.minus(localYear).minus(localMonth)

                        triggerDate = LocalDate.of(year,month,day)

                        if (triggerDate == LocalDate.now()){
                            scheduleNotification(this@MainActivity, model.triggerTime,model.id!!)
                        }
                    }
                }
            }
           if (scheduledAlarmStatus){
               val id = sharedPrefs.sharedPreferences.getInt("MODEL_ID",-1)
               if (id != -1){
                   CoroutineScope(Dispatchers.IO).launch {
                       useCases.getNoteUseCase(id).let {
                           model = it
                       }
                       scheduleNotification(this@MainActivity,model.triggerTime,model.id!!)
                   }
               }
           }
        }
    }

    private fun getModelId(): Int {
        var list: List<NotesModel> = emptyList()
        CoroutineScope(Dispatchers.IO).launch {
            useCases.getAllNotesUseCase.execute().let {
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
                    alarmStatus = note.alarmStatus,
                    triggerDate = note.triggerDate,
                    triggerTime = note.triggerTime
                )
                id = if (model.alarmStatus) {
                    model.id!!
                } else {
                    -1
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
    private fun scheduleNotification(ctx: Context, triggerTime: Long,requestCode:Int) {

        val alarmIntent = Intent(ctx, NotificationReceiver::class.java)
        val flag = PendingIntent.FLAG_IMMUTABLE
        val pendingIntent = PendingIntent.getBroadcast(ctx, requestCode, alarmIntent, flag)

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
    }
}