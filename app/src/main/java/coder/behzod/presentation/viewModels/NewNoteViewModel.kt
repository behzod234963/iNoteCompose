package coder.behzod.presentation.viewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coder.behzod.data.local.sharedPreferences.SharedPreferenceInstance
import coder.behzod.domain.model.NotesModel
import coder.behzod.domain.useCase.notesUseCases.NotesUseCases
import coder.behzod.presentation.utils.constants.KEY_ALARM_DATE_AND_TIME
import coder.behzod.presentation.utils.constants.colorsList
import coder.behzod.presentation.utils.events.NewNoteEvent
import coder.behzod.presentation.utils.helpers.NewNotesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewNoteViewModel @Inject constructor(
    private val useCases: NotesUseCases,
    savedStateHandle: SavedStateHandle,
    sharedPreferenceInstance: SharedPreferenceInstance
) : ViewModel() {

    private val _title = mutableStateOf(NewNotesState(""))
    val title: State<NewNotesState> = _title

    private val _localYear = mutableIntStateOf(1)
    val localYear:State<Int> = _localYear

    private val _alarmId = mutableIntStateOf( -1 )
    val alarmId:State<Int> = _alarmId

    private val _localMonth = mutableIntStateOf(1)
    val localMonth:State<Int> = _localMonth

    private val _localDay = mutableIntStateOf(1)
    val localDay:State<Int> = _localDay

    private val _alarmStatus = mutableStateOf( false )
    val alarmStatus:State<Boolean> = _alarmStatus

    var id = -1

    private val _note = mutableStateOf(NewNotesState(""))
    val note: State<NewNotesState> = _note

    private val _color = mutableIntStateOf(colorsList.random().toArgb())
    val color: State<Int> = _color

    private val _dateAndTime = mutableLongStateOf(0L)
    val dateAndTime:State<Long> = _dateAndTime

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
        sharedPreferenceInstance.sharedPreferences.edit().putLong(KEY_ALARM_DATE_AND_TIME,dateAndTime.value).apply()
    }

    fun saveNote(note: NotesModel) = viewModelScope.launch(Dispatchers.IO) {
        useCases.saveNoteUseCase(note)
    }

    fun saveLocalDate(year:Int,month:Int,day:Int) = viewModelScope.launch {
        _localYear.intValue = year
        _localMonth.intValue = month
        _localDay.intValue = day
    }

    fun saveAlarmStatus(status:Boolean) = viewModelScope.launch {
        _alarmStatus.value = status
    }

    fun saveId(id:Int) = viewModelScope.launch {
        _alarmId.intValue = id
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