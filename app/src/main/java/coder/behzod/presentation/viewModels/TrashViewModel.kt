package coder.behzod.presentation.viewModels

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coder.behzod.domain.model.NotesModel
import coder.behzod.domain.model.TrashModel
import coder.behzod.domain.useCase.notesUseCases.UseCases
import coder.behzod.domain.useCase.trashUseCases.TrashUseCases
import coder.behzod.presentation.utils.events.TrashEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TrashViewModel @Inject constructor (
    private val useCases: TrashUseCases,
    private val noteUseCases:UseCases
): ViewModel() {
    @SuppressLint("MutableCollectionMutableState")
    private val _trashedNotes = mutableStateOf(ArrayList<TrashModel>())
    val trashedNotes:State<ArrayList<TrashModel>> = _trashedNotes

    @SuppressLint("MutableCollectionMutableState")
    private val _selectedItems = mutableStateOf(ArrayList<TrashModel>())
    val selectedItems:State<ArrayList<TrashModel>> = _selectedItems

    private val _isItemSelected = mutableStateOf( false )
    val isItemSelected : State<Boolean> = _isItemSelected

    init {
        getNotes()
    }

    private fun getNotes() {
        viewModelScope.launch {
            useCases.getTrashedNotes().collect{
                it as ArrayList<TrashModel>
                _trashedNotes.value = it
            }
        }
    }
    fun delete(note:TrashModel) = viewModelScope.launch {
        useCases.delete(note)
    }
    fun multipleDelete(notes: ArrayList<TrashModel>) = viewModelScope.launch {
        useCases.multipleDelete(notes)
    }
    fun addToList(note:TrashModel) = viewModelScope.launch {
        _selectedItems.value.add(note)
    }
    fun addAllToList(notes:ArrayList<TrashModel>) = viewModelScope.launch {
        _selectedItems.value.addAll(notes)
    }
    fun removeFromList(note:TrashModel) = viewModelScope.launch {
        _selectedItems.value.remove(note)
    }
    fun restoreNote(note:NotesModel,trashModel: TrashModel) = viewModelScope.launch {
        noteUseCases.saveNoteUseCase(note)
        useCases.delete(trashModel)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun onEvent(event:TrashEvent, note: NotesModel? = null,notes: ArrayList<NotesModel>? = null, trashedNotes:ArrayList<TrashModel>? = null) = viewModelScope.launch {
        when(event){
            is TrashEvent.SelectAll->{
                _isItemSelected.value = event.isItemsSelected
            }
            is TrashEvent.RestoreAllNotes->{
                CoroutineScope(Dispatchers.Default).launch {
                    notes?.clear()
                    trashedNotes?.map {
                        it.title to note?.title
                        it.content to note?.note
                        it.daysLeft to note?.dataAdded?.compareTo(LocalDate.now().toString())
                        it.color to note?.color
                    }?.zip(notes!!)
                }
                CoroutineScope(Dispatchers.IO).launch {
                    notes?.let { useCases.restoreAllUseCase(it) }
                    delay(100)
                    if (trashedNotes != null) {
                        multipleDelete(trashedNotes)
                    }
                }
            }

            is TrashEvent.ClearList -> {
                event.list.clear()
            }
        }
    }
}