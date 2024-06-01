package coder.behzod.domain.useCase.notesUseCases

import coder.behzod.domain.model.NotesModel
import coder.behzod.domain.repository.NotesRepository

class DeleteAllUseCase(private val repository: NotesRepository) {
    suspend operator fun invoke(notes:ArrayList<NotesModel>){
        repository.deleteAll(notes)
    }
}