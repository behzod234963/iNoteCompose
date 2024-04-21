package coder.behzod.presentation.viewModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coder.behzod.domain.model.NotesModel
import coder.behzod.domain.useCase.UseCases
import coder.behzod.domain.utils.NoteOrder
import coder.behzod.domain.utils.OrderType
import coder.behzod.presentation.utils.helpers.NotesEvent
import coder.behzod.presentation.utils.helpers.NotesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val useCases: UseCases
):ViewModel(){
    private val _state = mutableStateOf(NotesState())
    val state : State<NotesState> = _state
    private var recentlyDeleted:NotesModel? = null
    private var getNotesJob: Job? = null
    init {
        getNotes(NoteOrder.Date(OrderType.Descending))
    }
    fun onEvent(event:NotesEvent){
        when(event){
            is NotesEvent.Order->{
                if (state.value.noteOrder::class == event.noteOrder::class &&
                    state.value.noteOrder.orderType == event.noteOrder.orderType){
                    return
                }
                getNotes(event.noteOrder)
            }
            is NotesEvent.DeleteNote->{
                viewModelScope.launch {
                    useCases.deleteUseCase(event.note)
                    recentlyDeleted = event.note
                }
            }
            is NotesEvent.RestoreNote->{
                viewModelScope.launch {
                    useCases.saveNoteUseCase(recentlyDeleted ?: return@launch)
                    recentlyDeleted = null
                }
            }
        }
    }
    fun getNotes(notesOrder:NoteOrder){
        getNotesJob?.cancel()
        getNotesJob = useCases.getNotesUseCase(noteOrder = notesOrder)
            .onEach {
                _state.value = state.value.copy(
                    noteOrder = notesOrder,
                )
            }.launchIn(viewModelScope)
    }
}