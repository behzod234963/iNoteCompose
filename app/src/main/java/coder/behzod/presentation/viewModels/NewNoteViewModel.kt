package coder.behzod.presentation.viewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coder.behzod.domain.model.NotesModel
import coder.behzod.domain.useCase.notesUseCases.NotesUseCases
import coder.behzod.presentation.utils.constants.colorList
import coder.behzod.presentation.utils.events.NewNoteEvent
import coder.behzod.presentation.utils.helpers.NewNotesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class NewNoteViewModel @Inject constructor(
    private val useCases: NotesUseCases,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _title = mutableStateOf(NewNotesState(""))
    val title: State<NewNotesState> = _title

    var id = -1

    private val _note = mutableStateOf(NewNotesState(""))
    val note: State<NewNotesState> = _note

    private val _color = mutableIntStateOf(colorList.random().toArgb())
    val color: State<Int> = _color

    private val _status = MutableLiveData(false)
    val status:LiveData<Boolean> = _status

    init {

        viewModelScope.launch {
            savedStateHandle.get<Int>("id")?.let {
                id = it
                if (it != -1) {
                    useCases.getNoteUseCase(it).also { notes ->
                        _title.value = title.value.copy(
                            text = notes.title
                        )
                        _note.value = note.value.copy(
                            text = notes.content
                        )
                        _color.intValue = notes.color
                    }
                }
            }
        }
    }

    fun saveNote(note: NotesModel) = viewModelScope.launch(Dispatchers.IO) {
        useCases.saveNoteUseCase(note)
    }

    fun newNoteEvent(event: NewNoteEvent) {
        when (event) {
            is NewNoteEvent.ChangedTitle -> {
                _title.value = note.value.copy(
                    text = event.title
                )
            }

            is NewNoteEvent.ChangedNote -> {
                _note.value = note.value.copy(
                    text = event.note
                )
            }

            is NewNoteEvent.NoteBackground -> {
                _color.intValue = event.color
            }
        }
    }
}