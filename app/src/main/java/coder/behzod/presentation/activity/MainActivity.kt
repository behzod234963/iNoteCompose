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
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.core.app.ActivityCompat
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import coder.behzod.R
import coder.behzod.data.local.dataStore.DataStoreInstance
import coder.behzod.data.local.sharedPreferences.SharedPreferenceInstance
import coder.behzod.data.workManager.workers.UpdateDayWorker
import coder.behzod.domain.model.NotesModel
import coder.behzod.domain.useCase.notesUseCases.NotesUseCases
import coder.behzod.presentation.broadcastReceiver.NotificationReceiver
import coder.behzod.presentation.navigation.NavGraph
import coder.behzod.presentation.notifications.NotificationTrigger
import coder.behzod.presentation.viewModels.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
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
            initUpdateDayWorkManager()
            InitAlarmManager()
            NavGraph()
            ManagerOnBackPressed()
        }
    }

    @Composable
    private fun ManagerOnBackPressed() {
        val count = remember { mutableIntStateOf(0) }
        val onBackPressed = onBackPressedDispatcher
        onBackPressed.addCallback {
            count.intValue++
        }
        LaunchedEffect(key1 = Boolean) {
            dataStoreInstance.getMainScreenState().collect { mainScreenState = it }
        }
       if (mainScreenState){
           if (count.intValue == 1) {
               Toast.makeText(
                   this@MainActivity,
                   stringResource(R.string.tap_twice_to_exit), Toast.LENGTH_SHORT
               ).show()
               Handler().postDelayed({
                   if (count.intValue != 2) {
                       count.intValue = 0
                   } else {
                       finish()
                   }
               }, 1000)
           }
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
                    } else {
                        val notificationReceiver =
                            Intent(this@MainActivity, NotificationReceiver::class.java)
                        val pendingIntent = PendingIntent.getBroadcast(
                            this@MainActivity,
                            model.requestCode,
                            notificationReceiver,
                            PendingIntent.FLAG_IMMUTABLE
                        )
                        alarmManager.cancel(pendingIntent)
                    }
                }
            }
        }
    }

    @SuppressLint("CoroutineCreationDuringComposition")
    @Composable
    private fun InitAlarmManager() {
        val id = dataStoreInstance.getModelId().collectAsState(initial = -1)
        val model = viewModel.model.value
        val notes = ArrayList<NotesModel>()

        if (id.value != -1) {
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.getNoteById(id.value)
            }
        }
        if (model?.alarmStatus == true) {
            notes.add(model)
            notificationTrigger.scheduleNotification(this@MainActivity, notes)
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

    private fun initUpdateDayWorkManager() {
        val updateDayRequest = PeriodicWorkRequestBuilder<UpdateDayWorker>(1, TimeUnit.DAYS).build()
        workManager.enqueue(updateDayRequest)
    }
}