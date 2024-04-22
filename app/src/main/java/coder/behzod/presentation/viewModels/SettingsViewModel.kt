package coder.behzod.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coder.behzod.data.local.sharedPreferences.SharedPreferenceInstance
import coder.behzod.presentation.utils.constants.KEY_INDEX
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val sharedPrefs: SharedPreferenceInstance
) : ViewModel() {

    private val _state = MutableLiveData(0f)
    val state: LiveData<Float> = _state
    private val _index = MutableLiveData<Int>()
    val index: LiveData<Int> = _index

    fun setIndex(index: Int) = viewModelScope.launch {
        sharedPrefs.sharedPreferences.edit().putInt(KEY_INDEX, index).apply()
    }

    fun getIndex() = viewModelScope.launch {
        val index = sharedPrefs.sharedPreferences.getInt(KEY_INDEX, 0)
        _index.postValue(index)
    }
}