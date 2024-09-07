package coder.behzod.di.modules

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.AlarmManagerCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavController
import coder.behzod.presentation.notifications.NotificationScheduler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.android.scopes.ServiceScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideNavController(@ApplicationContext ctx: Context): NavController = NavController(ctx)

    @Provides
    fun provideNotificationManager(@ApplicationContext ctx : Context):NotificationManagerCompat{
        val notificationManager = NotificationManagerCompat.from(ctx)
        val channel = NotificationChannel(
            "Main Channel ID",
            "Main Channel",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)
        return notificationManager
    }

    @Provides
    @Singleton
    fun provideNotificationScheduler(notificationManager: NotificationManagerCompat):NotificationScheduler{
        return NotificationScheduler(notificationManager)
    }
}