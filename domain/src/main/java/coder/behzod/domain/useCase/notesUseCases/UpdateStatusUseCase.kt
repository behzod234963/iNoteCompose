package coder.behzod.domain.useCase.notesUseCases

import coder.behzod.domain.repository.NotesRepository

class UpdateStatusUseCase(private val repository: NotesRepository) {

    suspend fun execute(requestCode:Int, status:Boolean){
        repository.updateStatus(requestCode = requestCode, status)
    }
}