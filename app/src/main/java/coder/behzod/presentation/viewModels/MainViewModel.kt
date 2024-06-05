package coder.behzod.presentation.viewModels

import android.annotation.SuppressLint
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coder.behzod.domain.model.NotesModel
import coder.behzod.domain.model.TrashModel
import coder.behzod.domain.useCase.notesUseCases.UseCases
import coder.behzod.domain.useCase.trashUseCases.SaveToTrashUseCase
import coder.behzod.domain.useCase.trashUseCases.TrashUseCases
import coder.behzod.domain.utils.NoteOrder
import coder.behzod.domain.utils.OrderType
import coder.behzod.presentation.utils.events.NotesEvent
import coder.behzod.presentation.utils.events.TrashEvent
import coder.behzod.presentation.utils.helpers.NotesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val useCases: UseCases,
    private val trashUseCase: TrashUseCases
):ViewModel(){

    private val _state = mutableStateOf(NotesState())
    val state : State<NotesState> = _state

    private var getNotesJob: Job? = null

    private val _selectAllStatus = mutableStateOf( false )
    val selectAllStatus:State<Boolean> = _selectAllStatus

    @SuppressLint("MutableCollectionMutableState")
    private val _selectedNotes = mutableStateOf( ArrayList<NotesModel>() )
    val selectedNotes:State<ArrayList<NotesModel>> = _selectedNotes

    init {
        getNotes(NoteOrder.Date(OrderType.Descending))
    }
    fun onEvent(event: NotesEvent){
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
                }
            }

            is NotesEvent.SelectAllStatus -> {
                _selectAllStatus.value = event.status
            }
        }
    }

    fun addAllToList() = viewModelScope.launch {
        _selectedNotes.value.addAll(state.value.notes)
    }

    fun addNoteToList(note:NotesModel) = viewModelScope.launch {
        _selectedNotes.value.add(note)
    }
    fun removeFromList(note:NotesModel){
        _selectedNotes.value.remove(note)
    }
    fun removeAllFromList(){
        _selectedNotes.value.clear()
    }
    fun saveToTrash(note: TrashModel) = viewModelScope.launch {
        trashUseCase.saveToTrash(note)
    }
    fun deleteAllUseCase(notes: ArrayList<NotesModel>) = viewModelScope.launch {
        useCases.deleteAllUseCase(notes)
    }

    fun getNotes(notesOrder:NoteOrder){
        getNotesJob?.cancel()
        getNotesJob = useCases.getNotesUseCase.invoke(noteOrder = notesOrder)
            .onEach {
                _state.value = state.value.copy(
                    notes = it,
                    noteOrder = notesOrder,
                )
            }.launchIn(viewModelScope)
    }
}