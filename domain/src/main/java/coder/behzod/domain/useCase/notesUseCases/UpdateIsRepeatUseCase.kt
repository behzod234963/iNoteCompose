package coder.behzod.domain.useCase.notesUseCases

import coder.behzod.domain.repository.NotesRepository

class UpdateIsRepeatUseCase(private val repository: NotesRepository) {

    suspend fun execute(id:Int,isRepeat:Boolean){
        repository.updateIsRepeat(id,isRepeat)
    }
}