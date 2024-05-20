package coder.behzod.domain.useCase.trashUseCases

import coder.behzod.domain.model.TrashModel
import coder.behzod.domain.repository.TrashRepository
import kotlinx.coroutines.flow.Flow

class GetTrashedNotesUseCase(private val repository: TrashRepository) {

    operator fun invoke():Flow<List<TrashModel>>{
        return repository.getTrashedNotes()
    }
}