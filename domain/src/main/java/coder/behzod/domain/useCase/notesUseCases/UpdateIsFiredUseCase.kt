package coder.behzod.domain.useCase.notesUseCases

import coder.behzod.domain.repository.NotesRepository

class UpdateIsFiredUseCase(private val repository: NotesRepository) {
    suspend fun execute(requestCode:Int,isFired:Boolean){
        repository.updateIsFired(requestCode,isFired)
    }
}