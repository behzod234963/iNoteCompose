package coder.behzod.domain.useCase.trashUseCases

import coder.behzod.domain.model.NotesModel
import coder.behzod.domain.repository.TrashRepository

class RestoreAllUseCase(private val repository: TrashRepository) {
    suspend operator fun invoke(notes:ArrayList<NotesModel>){
        repository.restoreAll(notes)
    }
}