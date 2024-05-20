package coder.behzod.domain.useCase.notesUseCases

import coder.behzod.domain.model.NotesModel
import coder.behzod.domain.repository.NotesRepository

class SaveNoteUseCase(private val repository: NotesRepository) {
    suspend operator fun invoke(note: NotesModel){
        repository.saveNote(note)
    }
}