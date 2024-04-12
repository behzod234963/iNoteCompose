package coder.behzod.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coder.behzod.data.local.sharedPreferences.SharedPreferenceInstance
import coder.behzod.presentation.utils.constants.KEY_INDEX
import coder.behzod.presentation.utils.constants.KEY_LANGUAGES
import coder.behzod.presentation.utils.constants.KEY_THEME_STATUS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val sharedPrefs: SharedPreferenceInstance
) : ViewModel() {

    private val _language = MutableLiveData(0)
    val language: LiveData<Int> = _language

    private val _state = MutableLiveData(0f)
    val state: LiveData<Float> = _state

    private val _langIndex = MutableLiveData<Int>()
    val langIndex: LiveData<Int> = _langIndex

    private val _status = MutableLiveData<Boolean>()
    val status: LiveData<Boolean> = _status

    private val _index = MutableLiveData<Int>()
    val index: LiveData<Int> = _index

    fun setStatus(status: Boolean) = viewModelScope.launch {
        sharedPrefs.sharedPreferences.edit().putBoolean(KEY_THEME_STATUS, status).apply()
    }

    fun getStatus() = viewModelScope.launch {
        val status = sharedPrefs.sharedPreferences.getBoolean(KEY_THEME_STATUS, true)
        _status.postValue(status)
    }

    fun setIndex(index: Int) = viewModelScope.launch {
        sharedPrefs.sharedPreferences.edit().putInt(KEY_INDEX, index).apply()
    }

    fun getIndex() = viewModelScope.launch {
        val index = sharedPrefs.sharedPreferences.getInt(KEY_INDEX, 0)
        _index.postValue(index)
    }

    fun getLangIndex() = viewModelScope.launch {
        val index = sharedPrefs.sharedPreferences.getInt(KEY_LANGUAGES, 0)
        _langIndex.postValue(index)
    }
}