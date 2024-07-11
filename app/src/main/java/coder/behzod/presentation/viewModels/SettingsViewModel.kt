package coder.behzod.presentation.viewModels

import androidx.appcompat.widget.ShareActionProvider
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import coder.behzod.data.local.dataStore.DataStoreInstance
import coder.behzod.data.local.sharedPreferences.SharedPreferenceInstance
import coder.behzod.presentation.utils.constants.KEY_INDEX
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val sharedPrefs:SharedPreferenceInstance
) : ViewModel() {

    private val _state = MutableLiveData(0f)
    val state: LiveData<Float> = _state
    private var _index = mutableIntStateOf(0)
    val index: State<Int> = _index

    fun setIndex(index: Int) = viewModelScope.launch {
        sharedPrefs.sharedPreferences.edit().putInt(KEY_INDEX,index).apply()
    }

    fun getIndex() = viewModelScope.launch {
        _index.intValue = sharedPrefs.sharedPreferences.getInt(KEY_INDEX,0)
    }
}