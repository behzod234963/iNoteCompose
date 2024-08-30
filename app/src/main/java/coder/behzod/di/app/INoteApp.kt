package coder.behzod.di.app

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import coder.behzod.data.workManager.factories.CheckDateWorkerFactory
import coder.behzod.data.workManager.factories.UpdateDayWorkerFactory
import coder.behzod.domain.useCase.notesUseCases.NotesUseCases
import coder.behzod.domain.useCase.trashUseCases.TrashUseCases
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class INoteApp : Application(),
    Configuration.Provider {

    @Inject
    lateinit var trashUseCases: TrashUseCases
    @Inject
    lateinit var useCases: NotesUseCases

    override val workManagerConfiguration: Configuration
        get() = Configuration
            .Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setWorkerFactory(UpdateDayWorkerFactory(useCases = trashUseCases))
            .setWorkerFactory(CheckDateWorkerFactory(useCases = useCases))
            .build()
}