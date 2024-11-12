package coder.behzod.presentation.activity

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.core.app.ActivityCompat
import androidx.work.WorkManager
import coder.behzod.R
import coder.behzod.data.local.dataStore.DataStoreInstance
import coder.behzod.data.local.sharedPreferences.SharedPreferenceInstance
import coder.behzod.domain.model.NotesModel
import coder.behzod.domain.useCase.notesUseCases.NotesUseCases
import coder.behzod.presentation.broadcastReceiver.NotificationReceiver
import coder.behzod.presentation.navigation.NavGraph
import coder.behzod.presentation.notifications.NotificationTrigger
import coder.behzod.presentation.utils.constants.notesModel
import coder.behzod.presentation.viewModels.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var workManager: WorkManager
    private lateinit var alarmManager: AlarmManager
    private lateinit var notificationTrigger: NotificationTrigger
    private val viewModel: MainActivityViewModel by viewModels()
    private var mainScreenState: Boolean = false

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
            CancelFiredAlarms()
            requestPermission()
            InitAlarmManager()
            NavGraph()
        }
    }
    @SuppressLint("MutableCollectionMutableState")
    @Composable
    private fun CancelFiredAlarms() {
        val notes = viewModel.notes.value
        if (notes.isNotEmpty()) {
            notes.forEach { model ->
                if (model.isFired && !model.isRepeat) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        alarmManager.cancelAll()
                        CoroutineScope(Dispatchers.IO).launch {
                            useCases.updateIsScheduledUseCase.execute(model.id,isScheduled = false)
                        }
                    } else {
                        val notificationReceiver =
                            Intent(this@MainActivity, NotificationReceiver::class.java)
                        val pendingIntent = PendingIntent.getBroadcast(
                            this@MainActivity,
                            model.id,
                            notificationReceiver,
                            PendingIntent.FLAG_IMMUTABLE
                        )
                        alarmManager.cancel(pendingIntent)
                        CoroutineScope(Dispatchers.IO).launch {
                            useCases.updateIsScheduledUseCase.execute(model.id,isScheduled = false)
                        }
                    }
                }
            }
        }
    }
    @SuppressLint("CoroutineCreationDuringComposition")
    @Composable
    private fun InitAlarmManager() {
        val id = dataStoreInstance.getModelId().collectAsState(initial = -1)
        val model = remember { mutableStateOf( notesModel ) }
        val notes = ArrayList<NotesModel>()

        if (id.value != -1) {
            CoroutineScope(Dispatchers.IO).launch {
                model.value = useCases.getNoteUseCase(id.value)
            }
        }
        if (model.value != null){
            if (model.value.alarmStatus && !model.value.isFired && !model.value.isRepeat) {
                notificationTrigger.scheduleNotification(this@MainActivity, model.value)
                CoroutineScope(Dispatchers.IO).launch {
                    useCases.updateIsScheduledUseCase.execute(model.value.id,true)
                }
            }else if (model.value.alarmStatus && model.value.isRepeat){
                notes.add(model.value)
                notificationTrigger.scheduleRepeatingNotification(this@MainActivity, model.value)
                CoroutineScope(Dispatchers.IO).launch {
                    useCases.updateIsScheduledUseCase.execute(model.value.id,true)
                }
            }
        }
    }
    @Composable
    private fun InitValue() {
        notificationTrigger = NotificationTrigger()
        alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        workManager = WorkManager.getInstance(applicationContext)
        Intent(this@MainActivity, MainActivity::class.java).also {
            mainScreenState = it.getBooleanExtra("MainScreen", false)
        }
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
}