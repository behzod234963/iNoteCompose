package coder.behzod.data

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import coder.behzod.domain.model.TrashModel
import coder.behzod.domain.useCase.trashUseCases.TrashUseCases
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmManager (private val ctx:Context){

    val alarmManager = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun alarmNotification(){
        val intent = Intent(ctx,AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(ctx,0,intent, PendingIntent.FLAG_IMMUTABLE)
        val requestTime = System.currentTimeMillis()
        alarmManager.setRepeating(AlarmManager.RTC,requestTime,AlarmManager.INTERVAL_DAY,pendingIntent)
        Log.d("alarmManager", "alarmManager: Alarm Manager is working")
    }
}

class AlarmReceiver(private val useCases: TrashUseCases) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        var notes: List<TrashModel> = emptyList()

        useCases.getListOfNotes.invoke().also {
            notes = it
        }

        Log.d("alarmManager", "alarmManager: ${notes.size}")
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
                Log.d("alarmManager", "alarmManager: increment value $increment ")

                model.id?.let { useCases.updateDayUseCase(it, increment) }.also {
                    Log.d("alarmManager", "alarmManager: note was updated $note")
                }
            }
        }
    }
}
