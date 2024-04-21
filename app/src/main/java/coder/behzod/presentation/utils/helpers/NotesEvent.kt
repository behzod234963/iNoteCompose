package coder.behzod.presentation.utils.helpers

import coder.behzod.domain.model.NotesModel
import coder.behzod.domain.utils.NoteOrder

sealed class NotesEvent {
    data class Order(val noteOrder: NoteOrder):NotesEvent()
    data class DeleteNote(val note:NotesModel):NotesEvent()
    data object RestoreNote:NotesEvent()
}