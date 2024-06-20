package coder.behzod.data.workManager.workers

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat

class UpdateDayNotification(private val ctx:Context) {

    val notificationManager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val channelId = "1"

    fun showNotification(title:String,content:String){
        val notification = NotificationCompat.Builder(ctx, channelId)
            .setContentText(content)
            .setContentTitle(title)
            .build()
        notificationManager.notify(1,notification)
    }
}