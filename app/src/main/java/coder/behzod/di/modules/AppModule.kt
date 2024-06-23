package coder.behzod.di.modules

import android.content.Context
import androidx.navigation.NavController
import coder.behzod.data.workManager.workers.UpdateDayWorkerFactory
import coder.behzod.domain.useCase.trashUseCases.TrashUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideNavController(@ApplicationContext ctx: Context): NavController = NavController(ctx)
}