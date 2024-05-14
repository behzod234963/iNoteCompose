package coder.behzod.presentation.utils.events

import androidx.core.view.WindowInsetsCompat.Type.InsetsType

sealed class NewNoteEvent {
    data class ChangedTitle(val title:String):NewNoteEvent()
    data class ChangedNote(val note:String):NewNoteEvent()
    data class NoteBackground(val color:Int):NewNoteEvent()
}