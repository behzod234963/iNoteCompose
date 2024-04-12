package coder.behzod.domain.useCase

import coder.behzod.domain.model.NotesModel
import coder.behzod.domain.repository.NotesRepository

class DeleteNoteUseCase(private val repository: NotesRepository) {
    suspend operator fun invoke(note: NotesModel){
        repository.deleteNote(note)
    }
}