package coder.behzod.presentation.viewModels

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coder.behzod.R
import coder.behzod.domain.model.NotesModel
import coder.behzod.domain.useCase.UseCases
import coder.behzod.presentation.utils.constants.colorList
import coder.behzod.presentation.utils.events.NewNoteEvent
import coder.behzod.presentation.utils.helpers.NewNotesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewNoteViewModel @Inject constructor(
    private val useCases: UseCases,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _title = mutableStateOf( NewNotesState("",) )
    val title: State<NewNotesState> = _title

    var id = -1

    private val _note = mutableStateOf(NewNotesState("",))
    val note: State<NewNotesState> = _note

    private val _color = mutableIntStateOf( colorList.random().toArgb() )
    val color : State<Int> = _color

    init {

       viewModelScope.launch {
           savedStateHandle.get<Int>("id")?.let {
               id = it
               if (it != -1){
                   useCases.getNoteUseCase(it).also {notes->
                       _title.value = title.value.copy(
                           text = notes.title
                       )
                       _note.value = note.value.copy(
                           text = notes.note
                       )
                       _color.intValue = notes.color
                   }
               }
           }
       }
    }

    fun saveNote(note: NotesModel) = viewModelScope.launch {
        useCases.saveNoteUseCase(note)
    }

    fun newNoteEvent(event:NewNoteEvent){
        when(event){
            is NewNoteEvent.ChangedTitle->{
                _title.value = note.value.copy(
                    text = event.title
                )
            }
            is NewNoteEvent.ChangedNote->{
                _note.value = note.value.copy(
                    text = event.note
                )
            }

            is NewNoteEvent.NoteBackground -> {
                _color.intValue = event.color
            }
        }
    }
    fun shareAndSaveNote(note: NotesModel, text: String, ctx: Context) = viewModelScope.launch {
        Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }.also { intent ->
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(
                ctx,
                Intent.createChooser(intent, ctx.getString(R.string.share_via)),
                null
            )
        }
        useCases.saveNoteUseCase(note)
    }
}
