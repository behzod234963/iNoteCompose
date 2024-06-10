package coder.behzod.presentation.utils.events

import coder.behzod.domain.model.NotesModel
import coder.behzod.domain.model.TrashModel

sealed class TrashEvent {
    data class SelectAll(val isItemsSelected: Boolean) : TrashEvent()
    data class RestoreAllNotes(
        val notesModelList: ArrayList<NotesModel>,
        val trashedNotesList: ArrayList<TrashModel>
    ) : TrashEvent()
    class RestoreSelected( ):TrashEvent()

    data class ClearList(val list: ArrayList<TrashModel>) : TrashEvent()
}