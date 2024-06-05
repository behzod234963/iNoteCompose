package coder.behzod.presentation.utils.events

import coder.behzod.domain.model.NotesModel
import coder.behzod.domain.model.TrashModel
import coder.behzod.domain.useCase.notesUseCases.UseCases

sealed class TrashEvent {
    data class SelectAll(val isItemsSelected:Boolean):TrashEvent()
    data class RestoreAllNotes(val restoreAll:ArrayList<NotesModel>):TrashEvent()
    data class ClearList(val list:ArrayList<TrashModel>):TrashEvent()
}