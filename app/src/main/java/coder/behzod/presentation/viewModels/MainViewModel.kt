package coder.behzod.presentation.viewModels

import android.annotation.SuppressLint
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coder.behzod.data.local.sharedPreferences.SharedPreferenceInstance
import coder.behzod.domain.model.NotesModel
import coder.behzod.domain.model.TrashModel
import coder.behzod.domain.useCase.notesUseCases.NotesUseCases
import coder.behzod.domain.useCase.trashUseCases.TrashUseCases
import coder.behzod.domain.utils.NoteOrder
import coder.behzod.domain.utils.OrderType
import coder.behzod.presentation.utils.constants.KEY_VIEW_TYPE
import coder.behzod.presentation.utils.events.NotesEvent
import coder.behzod.presentation.utils.helpers.NotesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val useCases: NotesUseCases,
    private val trashUseCase: TrashUseCases,
    sharedPrefs:SharedPreferenceInstance
) : ViewModel() {

    private val _state = mutableStateOf(NotesState())
    val state: State<NotesState> = _state

    private val _selectAll = mutableStateOf(false)
    val selectAll:State<Boolean> = _selectAll

    private var getNotesJob: Job? = null

    @SuppressLint("MutableCollectionMutableState")
    private val _selectedNotes = mutableStateOf(ArrayList<NotesModel>())
    val selectedNotes: State<ArrayList<NotesModel>> = _selectedNotes

    private val _viewType = mutableIntStateOf(sharedPrefs.sharedPreferences.getInt(KEY_VIEW_TYPE,0))
    val viewType:State<Int> = _viewType

    init {
        getNotes(NoteOrder.Date(OrderType.Descending))
    }

    fun updateAlarmStatus(requestCode:Int,status:Boolean) = viewModelScope.launch {
        useCases.updateStatusUseCase.execute(requestCode,status)
    }
    fun updateIsRepeat(requestCode: Int,isRepeat:Boolean) = viewModelScope.launch {
        useCases.updateIsRepeatUseCase.execute(requestCode,isRepeat)
    }
    fun onEvent(event: NotesEvent) {
        when (event) {
            is NotesEvent.Order -> {
                if (state.value.noteOrder::class == event.noteOrder::class &&
                    state.value.noteOrder.orderType == event.noteOrder.orderType
                ) {
                    return
                }
                getNotes(event.noteOrder)
            }

            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    useCases.deleteUseCase(event.note)
                }
            }

            is NotesEvent.ViewType->{
                _viewType.intValue = event.viewType
            }
            is NotesEvent.SelectAll->{
                _selectAll.value = event.status
            }
        }
    }

    fun multipleDelete(notes: ArrayList<NotesModel>) {
        viewModelScope.launch(Dispatchers.IO) {
            useCases.deleteAllUseCase(notes)
        }
    }

    fun addAllToList(notes:List<NotesModel>) = viewModelScope.launch {
        for (note in notes){
            _selectedNotes.value.add(note)
        }
    }

    fun addNoteToList(note: NotesModel) = viewModelScope.launch {
        _selectedNotes.value.add(note)
    }

    fun removeFromList(note: NotesModel) {
        _selectedNotes.value.remove(note)
    }

    fun clearList() {
        _selectedNotes.value.clear()
    }

    fun saveAllToTrash(notesModel: List<NotesModel>) {
        viewModelScope.launch(Dispatchers.IO) {
            for (notes in notesModel) {
                val trashModel = TrashModel(
                    id = notes.id,
                    title = notes.title,
                    content = notes.content,
                    color = notes.color,
                    daysLeft = 30
                )
                trashUseCase.saveToTrashUseCase(trashModel)
                onEvent(
                    NotesEvent.DeleteNote(
                        notes
                    )
                )
            }
        }
    }

    fun saveToTrash(note: TrashModel) = viewModelScope.launch {
        trashUseCase.saveToTrashUseCase(note)
    }

    fun returnDeletedNote(note: NotesModel) {
        CoroutineScope(Dispatchers.IO).launch {
            useCases.saveNoteUseCase(note)
        }
    }

    fun getNotes(notesOrder: NoteOrder) {
        getNotesJob?.cancel()
        getNotesJob = useCases.getNotesUseCase.invoke(noteOrder = notesOrder)
            .onEach {
                _state.value = state.value.copy(
                    notes = it,
                    noteOrder = notesOrder,
                )
            }.launchIn(viewModelScope)
    }
    fun updateIsScheduled(id:Int,isScheduled:Boolean) = viewModelScope.launch {
        useCases.updateIsScheduledUseCase.execute(id,isScheduled)
    }
}