package coder.behzod.domain.useCase

import coder.behzod.domain.model.NotesModel
import coder.behzod.domain.repository.NotesRepository
import coder.behzod.domain.utils.NoteOrder
import coder.behzod.domain.utils.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetNotesUseCase(private val repository: NotesRepository) {
    operator fun invoke(
        noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending)
    ): Flow<List<NotesModel>>
    {
        return repository.getNotes().map { notes ->
            when(noteOrder.orderType){
                is OrderType.Ascending->{
                    when(noteOrder){
                        is NoteOrder.Title-> notes.sortedBy { it.title}
                        is NoteOrder.Date-> notes.sortedBy { it.dataAdded }
                        is NoteOrder.Color-> notes.sortedBy { it.color }
                    }
                }
                is OrderType.Descending->{
                    when(noteOrder){
                        is NoteOrder.Title-> { notes .sortedByDescending { it.title }}
                        is NoteOrder.Date-> notes.sortedByDescending { it.dataAdded }
                        is NoteOrder.Color-> notes.sortedByDescending { it.color }
                    }
                }
            }
        }
    }
}