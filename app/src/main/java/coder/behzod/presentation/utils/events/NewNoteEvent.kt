package coder.behzod.presentation.utils.events

import androidx.core.view.WindowInsetsCompat.Type.InsetsType
import coder.behzod.domain.model.NotesModel

sealed class NewNoteEvent {
    data class Model(val model: NotesModel? = null):NewNoteEvent()
    data class ChangedTitle(val title:String):NewNoteEvent()
    data class ChangedNote(val note:String):NewNoteEvent()
    data class NoteBackground(val color:Int):NewNoteEvent()
}