package coder.behzod.domain.useCase.notesUseCases

import coder.behzod.domain.repository.NotesRepository

class UpdateStatusUseCase(private val repository: NotesRepository) {

    suspend fun execute(id:Int,status:Boolean){
        repository.updateStatus(id = id, status)
    }
}