package coder.behzod.presentation.utils.events

import coder.behzod.domain.model.NotesModel
import coder.behzod.domain.useCase.notesUseCases.UseCases

sealed class TrashEvent {
    class SelectAll(var isItemsSelected:Boolean):TrashEvent()
    data class RestoreAllNotes(val restoreAll:ArrayList<NotesModel>):TrashEvent()
}