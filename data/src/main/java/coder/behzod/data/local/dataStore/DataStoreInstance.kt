package coder.behzod.data.local.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "status")

class DataStoreInstance(private val ctx: Context) {

    suspend fun saveModelId(id:Int){
        val key = intPreferencesKey("id")
        ctx.dataStore.edit {modelId->
            modelId[key] = id
        }
    }
    fun getModelId():Flow<Int>{
        val key = intPreferencesKey("id")
        val modelId = ctx.dataStore.data.map { id->
            id[key]?:-1
        }
        return modelId
    }

    suspend fun selectAllStatus(status:Boolean){
        val statusKey = booleanPreferencesKey("statusKey")
        ctx.dataStore.edit {
            it[statusKey] = status
        }
    }
    fun getStatus():Flow<Boolean>{
        val statusKey = booleanPreferencesKey("statusKey")
        val status = ctx.dataStore.data.map {
            it[statusKey]?:true
        }
        return status
    }
    suspend fun mainScreenState(status:Boolean){
        val statusKey = booleanPreferencesKey("statusKey")
        ctx.dataStore.edit {
            it[statusKey] = status
        }
    }
    fun getMainScreenState():Flow<Boolean>{
        val statusKey = booleanPreferencesKey("statusKey")
        val status = ctx.dataStore.data.map {
            it[statusKey]?:false
        }
        return status
    }
}