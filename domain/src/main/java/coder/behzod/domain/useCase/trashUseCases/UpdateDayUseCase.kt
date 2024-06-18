package coder.behzod.domain.useCase.trashUseCases

import coder.behzod.domain.repository.TrashRepository

class UpdateDayUseCase(private val repository: TrashRepository) {

    suspend operator fun invoke(id:Int,day:Int){
        repository.updateDay(id, day)
    }
}