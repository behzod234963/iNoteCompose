package coder.behzod.di.modules

import coder.behzod.data.implementations.NoteRepositoryImpl
import coder.behzod.data.implementations.TrashRepositoryImpl
import coder.behzod.data.local.room.NotesDao
import coder.behzod.data.local.room.TrashDao
import coder.behzod.domain.repository.NotesRepository
import coder.behzod.domain.repository.TrashRepository
import coder.behzod.domain.useCase.notesUseCases.DeleteNoteUseCase
import coder.behzod.domain.useCase.notesUseCases.GetNoteUseCase
import coder.behzod.domain.useCase.notesUseCases.GetNotesUseCase
import coder.behzod.domain.useCase.notesUseCases.SaveNoteUseCase
import coder.behzod.domain.useCase.notesUseCases.UseCases
import coder.behzod.domain.useCase.trashUseCases.DeleteAllUseCase
import coder.behzod.domain.useCase.trashUseCases.DeleteUseCase
import coder.behzod.domain.useCase.trashUseCases.GetTrashedNotesUseCase
import coder.behzod.domain.useCase.trashUseCases.RestoreAllUseCase
import coder.behzod.domain.useCase.trashUseCases.SaveToTrashUseCase
import coder.behzod.domain.useCase.trashUseCases.TrashUseCases
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
    fun provideNoteUseCases(repository: NotesRepository): UseCases =
        UseCases(
            deleteUseCase = DeleteNoteUseCase(repository),
            getNotesUseCase = GetNotesUseCase(repository),
            getNoteUseCase = GetNoteUseCase(repository),
            saveNoteUseCase = SaveNoteUseCase(repository)
        )

    @Provides
    @Singleton
    fun provideTrashUseCases(repository: TrashRepository):TrashUseCases =
        TrashUseCases(
            delete = DeleteUseCase(repository),
            deleteAll = DeleteAllUseCase(repository),
            getTrashedNotes = GetTrashedNotesUseCase(repository),
            saveToTrash = SaveToTrashUseCase(repository),
            restoreAllUseCase = RestoreAllUseCase(repository)
        )

    @Provides
    @Singleton
    fun provideTrashRepository(trashDao:TrashDao):TrashRepository = TrashRepositoryImpl(trashDao)
    @Provides
    @Singleton
    fun provideNoteRepository(dao: NotesDao):NotesRepository = NoteRepositoryImpl(dao)
}