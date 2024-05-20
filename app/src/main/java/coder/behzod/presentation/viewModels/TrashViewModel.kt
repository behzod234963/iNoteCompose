package coder.behzod.presentation.viewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
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


    private val _isSelected = mutableStateOf(TrashEvent())
    val isSelected: State<TrashEvent.IsSelected> = _isSelected
    init {
        getNotes()
    }

    private fun getNotes() {
        viewModelScope.launch {
            useCases.getTrashedNotes()
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
                event.isSelected = true
            }
        }
    }
}