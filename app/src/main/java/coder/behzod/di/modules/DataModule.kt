package coder.behzod.di.modules

import android.app.Application
import android.content.Context
import androidx.room.Room
import coder.behzod.data.local.room.NotesDao
import coder.behzod.data.local.room.RoomInstance
import coder.behzod.data.local.sharedPreferences.SharedPreferenceInstance
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {
    @Provides
    @Singleton
    fun provideRoomDatabase(app: Application): RoomInstance =
        Room.databaseBuilder(
            app,
            RoomInstance::class.java,
            RoomInstance.ROOM_DB_NAME
        ).build()

    @Provides
    @Singleton
    fun provideNotesDao(room:RoomInstance): NotesDao = room.dao

    @Provides
    @Singleton
    fun provideSharedPreferenceInstance(@ApplicationContext ctx: Context): SharedPreferenceInstance = SharedPreferenceInstance(
        ctx,
    )
}