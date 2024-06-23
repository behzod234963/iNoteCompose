package coder.behzod.di.app

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import coder.behzod.data.local.room.TrashDao
import coder.behzod.data.workManager.workers.UpdateDayWorkerFactory
import coder.behzod.domain.useCase.trashUseCases.TrashUseCases
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class INoteApp : Application(),
    Configuration.Provider {

    @Inject
    lateinit var useCases: TrashUseCases

    override val workManagerConfiguration: Configuration
        get() = Configuration
            .Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setWorkerFactory(UpdateDayWorkerFactory(useCases = useCases))
            .build()

}