package coder.behzod.domain.useCase.trashUseCases

import coder.behzod.domain.model.TrashModel
import coder.behzod.domain.repository.TrashRepository

class DeleteAllUseCase (private val repository: TrashRepository){

    suspend operator fun invoke(notes:List<TrashModel>){
        repository.deleteAll(notes)
    }
}