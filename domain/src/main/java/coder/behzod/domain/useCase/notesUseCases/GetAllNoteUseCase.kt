package coder.behzod.domain.useCase.notesUseCases

import coder.behzod.domain.repository.NotesRepository

class GetAllNoteUseCase(private val repository: NotesRepository) {

    fun execute() = repository.getAllNotes()
}