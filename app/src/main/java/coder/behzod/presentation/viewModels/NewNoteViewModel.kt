package coder.behzod.presentation.viewModels

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coder.behzod.R
import coder.behzod.domain.model.NotesModel
import coder.behzod.domain.useCase.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewNoteViewModel @Inject constructor(
    private val useCases: UseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _title = MutableLiveData("")
    val title: LiveData<String> = _title

    private val _note = MutableLiveData("")
    val note: LiveData<String> = _note

    fun getNote(id: Int) = viewModelScope.launch {
        useCases.getNoteUseCase(id).also {
            _title.value = it.title
            _note.value = it.note
        }
    }

    fun saveNote(note: NotesModel) = viewModelScope.launch {
        useCases.saveNoteUseCase(note)
    }

    fun shareAndSaveNote(note: NotesModel, text: String, ctx: Context) = viewModelScope.launch {
        Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }.also { intent ->
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(
                ctx,
                Intent.createChooser(intent, ctx.getString(R.string.share_via)),
                null
            )
        }
        useCases.saveNoteUseCase(note)
    }
}
