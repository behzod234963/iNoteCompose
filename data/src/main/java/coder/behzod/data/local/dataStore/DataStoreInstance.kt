package coder.behzod.data.local.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map


private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "status")

class DataStoreInstance(private val ctx:Context) {

    suspend fun saveStatus(key:String, status:Boolean){
        val statusKey = booleanPreferencesKey(key)
        ctx.dataStore.edit {
            it[statusKey] = status
        }
    }
    fun getStatus(key:String): Flow<Boolean> {
        val statusKey = booleanPreferencesKey(key)
        val flow: Flow<Boolean> = ctx.dataStore.data
            .map {
                it[statusKey] == true
            }
        return flow
    }
}