package coder.behzod.domain.useCase.trashUseCases

import coder.behzod.domain.model.TrashModel
import coder.behzod.domain.repository.NotesRepository
import coder.behzod.domain.repository.TrashRepository

class SaveToTrashUseCase(private val repository: TrashRepository) {

    suspend operator fun invoke(note: TrashModel){
        repository.saveToTrash(note)
    }
}