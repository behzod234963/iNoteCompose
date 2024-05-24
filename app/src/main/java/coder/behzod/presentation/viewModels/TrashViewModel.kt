package coder.behzod.presentation.viewModels

import android.annotation.SuppressLint
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coder.behzod.domain.model.TrashModel
import coder.behzod.domain.useCase.trashUseCases.TrashUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrashViewModel @Inject constructor (
    private val useCases: TrashUseCases
): ViewModel() {
    @SuppressLint("MutableCollectionMutableState")
    private val _trashedNotes = mutableStateOf(ArrayList<TrashModel>())
    val trashedNotes:State<ArrayList<TrashModel>> = _trashedNotes

    @SuppressLint("MutableCollectionMutableState")
    private val _selectedItems = mutableStateOf(ArrayList<TrashModel>())
    val selectedItems:State<ArrayList<TrashModel>> = _selectedItems
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
    fun deleteAll(notes:ArrayList<TrashModel>) = viewModelScope.launch {
        useCases.deleteAll(notes)
        notes.clear()
    }
    fun addToList(note:TrashModel) = viewModelScope.launch {
        _selectedItems.value.add(note)
    }
    fun removeFromList(note:TrashModel) = viewModelScope.launch {
        _selectedItems.value.remove(note)
    }
}