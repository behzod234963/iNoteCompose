package coder.behzod.domain.useCase.trashUseCases

import coder.behzod.domain.repository.NotesRepository
import coder.behzod.domain.repository.TrashRepository

class GetListOfNotes(private val repository: TrashRepository) {

    operator fun invoke() = repository.getListOfNotes()
}