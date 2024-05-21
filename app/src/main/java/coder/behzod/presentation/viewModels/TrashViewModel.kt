package coder.behzod.presentation.viewModels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coder.behzod.domain.model.TrashModel
import coder.behzod.domain.useCase.trashUseCases.TrashUseCases
import coder.behzod.presentation.utils.events.TrashEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrashViewModel @Inject constructor (
    private val useCases: TrashUseCases
): ViewModel() {


    private val _isSelected = mutableStateOf(false)
    val isSelected: State<Boolean> = _isSelected

    private val _trashedNotes = MutableLiveData<ArrayList<TrashModel>>()
    val trashedNotes:LiveData<ArrayList<TrashModel>> = _trashedNotes
    init {
        getNotes()
    }

    private fun getNotes() {
        Log.d("fix", "TrashScreen: ${trashedNotes.value?.size}")
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
    }
    fun onEvent(event:TrashEvent){
        when(event){
            is TrashEvent.IsSelected->{
                _isSelected.value = event.isSelected
            }
        }
    }
}