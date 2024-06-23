package coder.behzod.data.workManager.workers

import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import coder.behzod.data.R

class UpdateDayNotification(private val ctx:Context) {



    fun showNotification(title:String,content:String){
        if (Build.VERSION.SDK_INT<=Build.VERSION_CODES.O){

        }
    }
}