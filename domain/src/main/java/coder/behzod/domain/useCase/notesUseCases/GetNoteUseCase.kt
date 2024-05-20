package coder.behzod.domain.useCase.notesUseCases

import coder.behzod.domain.model.NotesModel
import coder.behzod.domain.repository.NotesRepository

class GetNoteUseCase(private val repository: NotesRepository) {
    suspend operator fun invoke(id:Int): NotesModel = repository.getNote(id)
}