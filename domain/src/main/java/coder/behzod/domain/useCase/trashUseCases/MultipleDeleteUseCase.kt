package coder.behzod.domain.useCase.trashUseCases

import coder.behzod.domain.model.TrashModel
import coder.behzod.domain.repository.TrashRepository

class MultipleDeleteUseCase (private val repository: TrashRepository){

    suspend operator fun invoke(notes:ArrayList<TrashModel>) {
        repository.multipleDelete(notes)
    }
}