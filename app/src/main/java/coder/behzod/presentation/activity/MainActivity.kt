package coder.behzod.presentation.activity

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import coder.behzod.data.local.dataStore.DataStoreInstance
import coder.behzod.data.local.sharedPreferences.SharedPreferenceInstance
import coder.behzod.data.workManager.workers.CheckDateWorker
import coder.behzod.data.workManager.workers.UpdateDayWorker
import coder.behzod.domain.useCase.notesUseCases.NotesUseCases
import coder.behzod.presentation.alarmManager.schedulers.AlarmScheduler
import coder.behzod.presentation.navigation.NavGraph
import coder.behzod.presentation.notifications.NotificationTrigger
import coder.behzod.presentation.utils.constants.noteModel
import coder.behzod.presentation.viewModels.NewNoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var workManager: WorkManager
    private lateinit var alarmManager: AlarmManager
    private lateinit var notificationTrigger: NotificationTrigger
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
            initUpdateDayWorkManager()
            InitAlarmManager()
            NavGraph()
        }
    }

    @SuppressLint("CoroutineCreationDuringComposition")
    @Composable
    private fun InitAlarmManager() {

        val id = dataStoreInstance.getModelId().collectAsState(initial = -1)
        val model = remember { mutableStateOf( noteModel ) }

        if (id.value != -1){
            CoroutineScope(Dispatchers.IO).launch {
                model.value = useCases.getNoteUseCase(id.value)
                dataStoreInstance.saveModelId(-1)
            }
        }
        if (model.value.alarmStatus){
            notificationTrigger.scheduleNotification(this@MainActivity,model.value.triggerTime,model.value.requestCode)
            sharedPrefs.sharedPreferences.edit().putString("title",model.value.title).apply()
            sharedPrefs.sharedPreferences.edit().putString("content",model.value.content).apply()
            sharedPrefs.sharedPreferences.edit().putInt("requestCode",model.value.requestCode).apply()
            sharedPrefs.sharedPreferences.edit().putInt("stopCode",model.value.stopCode).apply()
        }
    }
    @Composable
    private fun InitValue() {
        notificationTrigger = NotificationTrigger(this@MainActivity)
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
    private fun initUpdateDayWorkManager() {

        val updateDayRequest = PeriodicWorkRequestBuilder<UpdateDayWorker>(1, TimeUnit.DAYS).build()
        workManager.enqueueUniquePeriodicWork(
            "Update day Worker",
            ExistingPeriodicWorkPolicy.UPDATE,
            updateDayRequest
        )
    }
}