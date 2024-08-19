package coder.behzod.presentation.alarmManager.schedulers

import android.content.Context
import coder.behzod.data.local.sharedPreferences.SharedPreferenceInstance
import coder.behzod.domain.model.NotesModel
import coder.behzod.presentation.notifications.NotificationTrigger
import coder.behzod.presentation.utils.constants.KEY_LOCAL_DAY
import coder.behzod.presentation.utils.constants.KEY_LOCAL_MONTH
import coder.behzod.presentation.utils.constants.KEY_LOCAL_YEAR
import coder.behzod.presentation.viewModels.MainViewModel
import java.time.LocalDate

class AlarmScheduler(private val ctx:Context,private val model: NotesModel,private val sharedPrefs:SharedPreferenceInstance,private val viewModel:MainViewModel) {

    private val notificationTrigger = NotificationTrigger(ctx)

    private var localYear = sharedPrefs.sharedPreferences.getInt(KEY_LOCAL_YEAR,1)
    private var localMonth = sharedPrefs.sharedPreferences.getInt(KEY_LOCAL_MONTH,1)
    private var localDay = sharedPrefs.sharedPreferences.getInt(KEY_LOCAL_DAY,1)
    private var year = 0
    private var month = 0
    private var day = 0

    fun execute(){
        if (model.alarmStatus){

            year = model.triggerDate.minus(localMonth).minus(localDay)
            month = model.triggerDate.minus(localYear).minus(localDay)
            day = model.triggerDate.minus(localYear).minus(localMonth)

            val currentDate = LocalDate.now()
            val currentYear = currentDate.year
            val currentMonth = currentDate.month.value
            val currentDay = currentDate.dayOfMonth

            if (year == currentYear && month == currentMonth && day == currentDay){

                notificationTrigger.scheduleNotification(ctx, model.triggerTime, model.requestCode)
            }
            sharedPrefs.sharedPreferences.edit().putBoolean("KEY_CURRENT_DAY_ALARM_STATUS",true).apply()
            sharedPrefs.sharedPreferences.edit().putInt("MODEL_ID",model.id!!).apply()
        }
    }
}