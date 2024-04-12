package coder.behzod.di.modules

import coder.behzod.data.implementations.NoteRepositoryImpl
import coder.behzod.data.local.room.NotesDao
import coder.behzod.domain.repository.NotesRepository
import coder.behzod.domain.useCase.DeleteNoteUseCase
import coder.behzod.domain.useCase.GetNoteUseCase
import coder.behzod.domain.useCase.GetNotesUseCase
import coder.behzod.domain.useCase.SaveNoteUseCase
import coder.behzod.domain.useCase.UseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DomainModule {
    @Provides
    @Singleton
    fun provideUseCases(repository: NotesRepository): UseCases =
        UseCases(
            deleteUseCase = DeleteNoteUseCase(repository),
            getNotesUseCase = GetNotesUseCase(repository),
            getNoteUseCase = GetNoteUseCase(repository),
            saveNoteUseCase = SaveNoteUseCase(repository)
        )

    @Provides
    @Singleton
    fun provideNoteRepository(dao: NotesDao):NotesRepository = NoteRepositoryImpl(dao)
}