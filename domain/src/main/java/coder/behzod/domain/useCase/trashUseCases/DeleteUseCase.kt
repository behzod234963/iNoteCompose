package coder.behzod.domain.useCase.trashUseCases

import coder.behzod.domain.model.TrashModel
import coder.behzod.domain.repository.TrashRepository

class DeleteUseCase(private val repository: TrashRepository) {

    suspend operator fun invoke(note:TrashModel){
        repository.delete(note)
    }
}