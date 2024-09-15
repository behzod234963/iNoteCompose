package coder.behzod.presentation.notifications

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import coder.behzod.R
import coder.behzod.presentation.activity.MainActivity
import javax.inject.Inject

class NotificationScheduler @Inject constructor(
    private val notificationManager: NotificationManagerCompat,
) {

    @RequiresApi(Build.VERSION_CODES.P)
    fun showNotification(ctx: Context,requestCode:Int, title: String, content: String) {

        val contentIntent =
            Intent(ctx, MainActivity::class.java).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK }
        val contentIntentPendingIntent =
            PendingIntent.getActivity(ctx, 1, contentIntent, PendingIntent.FLAG_IMMUTABLE)

        if (ActivityCompat.checkSelfPermission(
                ctx,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            notificationManager.notify(
                requestCode,
                createNotification(
                    ctx,
                    title,
                    content,
                    contentIntentPendingIntent
                ).build()
            )
        }
    }

    private fun createNotification(
        ctx: Context,
        title: String,
        content: String,
        contentPendingIntent: PendingIntent,
    ): NotificationCompat.Builder {

        val notificationWithoutAction = NotificationCompat.Builder(ctx, "Main Channel ID")
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContentIntent(contentPendingIntent)
            .setOngoing(false)
            .setAutoCancel(true)
        return notificationWithoutAction
    }
}