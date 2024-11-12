package coder.behzod.domain.useCase.notesUseCases

import coder.behzod.domain.repository.NotesRepository

class UpdateIsFiredUseCase(private val repository: NotesRepository) {
    suspend fun execute(id:Int,isFired:Boolean){
        repository.updateIsFired(id,isFired)
    }
}