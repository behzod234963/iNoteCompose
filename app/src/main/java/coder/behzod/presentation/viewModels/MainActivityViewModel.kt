package coder.behzod.presentation.viewModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coder.behzod.domain.model.NotesModel
import coder.behzod.domain.useCase.notesUseCases.NotesUseCases
import coder.behzod.presentation.utils.constants.notesModel
import coder.behzod.presentation.utils.helpers.NotesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val notesUseCases: NotesUseCases,
) :ViewModel(){
    private val _notes = mutableStateOf(NotesState().notes)
    val notes : State<List<NotesModel>> = _notes
    private val _model = MutableLiveData<NotesModel?>()
    val model: MutableLiveData<NotesModel?> = _model

    init {
        getALlNotes()
    }

    private fun getALlNotes() = viewModelScope.launch{
        _notes.value = notesUseCases.getAllNotesUseCase.execute()
    }
    fun getNoteById(id:Int) = viewModelScope.launch {
        _model.value = notesUseCases.getNoteUseCase(id)
    }
}