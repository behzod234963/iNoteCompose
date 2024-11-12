package coder.behzod.domain.useCase.notesUseCases

import coder.behzod.domain.repository.NotesRepository

class UpdateIsScheduledUseCase(private val notesRepository: NotesRepository) {

    suspend fun execute(id: Int, isScheduled: Boolean) {
        notesRepository.updateIsScheduled(id, isScheduled)
    }
}