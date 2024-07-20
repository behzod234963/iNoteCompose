package coder.behzod.data.local.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "status")

class DataStoreInstance(private val ctx: Context) {

    suspend fun saveColorIndex(key: String, index: Int) {
        val indexKey = intPreferencesKey(key)
        ctx.dataStore.edit {
            it[indexKey] = index
        }
    }

    fun getColorIndex(key: String): Flow<Int> {
        val indexKey = intPreferencesKey(key)
        val flow: Flow<Int> = ctx.dataStore.data
            .map {
                it[indexKey]!!
            }
        return flow
    }

    suspend fun saveStatus(key: String, status: Boolean) {
        val statusKey = booleanPreferencesKey(key)
        ctx.dataStore.edit {
            it[statusKey] = status
        }
    }

    fun getStatus(key: String): Flow<Boolean> {
        val statusKey = booleanPreferencesKey(key)
        val flow: Flow<Boolean> = ctx.dataStore.data
            .map {
                it[statusKey] == true
            }
        return flow
    }
    suspend fun saveTrigger(key:String,trigger:Long){
        val longKey = longPreferencesKey(key)
        ctx.dataStore.edit {
            it[longKey] = trigger
        }
    }
    fun getTrigger(key: String):Flow<Long>{
        val longKey = longPreferencesKey(key)
        val triggerFlow :Flow<Long> = ctx.dataStore.data
            .map {
                it[longKey]!!
            }
        return triggerFlow
    }
}