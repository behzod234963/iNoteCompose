package coder.behzod.presentation.utils.events

import coder.behzod.domain.model.NotesModel
import coder.behzod.domain.utils.NoteOrder

sealed class NotesEvent {
    data class Order(val noteOrder: NoteOrder): NotesEvent()
    data class DeleteNote(val note:NotesModel): NotesEvent()
    data class SelectAllStatus(val status:Boolean):NotesEvent()
    data class ViewType(val viewType: Int):NotesEvent()
}