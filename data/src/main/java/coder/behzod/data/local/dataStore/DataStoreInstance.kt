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
import java.util.function.BooleanSupplier


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "status")

class DataStoreInstance(private val ctx: Context) {

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

    suspend fun saveId(key: String,id:Int){

        val intKey = intPreferencesKey(key)
        ctx.dataStore.edit {
            it[intKey] = id
        }
    }

    fun getId(key: String):Flow<Int>{

        val idKey = intPreferencesKey(key)
        val idFlow : Flow<Int> = ctx.dataStore.data
            .map {
                it[idKey]!!
            }
        return idFlow
    }

    suspend fun saveYear(key:String,year:Int){

        val dateKey = intPreferencesKey(key)
        ctx.dataStore.edit {
            it[dateKey] = year
        }
    }

    fun getYear(key: String):Flow<Int>{

        val dateKey = intPreferencesKey(key)
        val dateFlow:Flow<Int> = ctx.dataStore.data
            .map {
                it[dateKey]!!
            }
        return dateFlow
    }

    suspend fun saveMonth(key:String,month:Int){

        val dateKey = intPreferencesKey(key)
        ctx.dataStore.edit {
            it[dateKey] = month
        }
    }

    fun getMonth(key: String):Flow<Int>{

        val dateKey = intPreferencesKey(key)
        val dateFlow:Flow<Int> = ctx.dataStore.data
            .map {
                it[dateKey]!!
            }
        return dateFlow
    }

    suspend fun saveDay(key:String,day:Int){

        val dateKey = intPreferencesKey(key)
        ctx.dataStore.edit {
            it[dateKey] = day
        }
    }

    fun getDay(key: String):Flow<Int>{

        val dateKey = intPreferencesKey(key)
        val dateFlow:Flow<Int> = ctx.dataStore.data
            .map {
                it[dateKey]!!
            }
        return dateFlow
    }


    fun getTrigger(key: String):Flow<Long>{

        val longKey = longPreferencesKey(key)
        val triggerFlow :Flow<Long> = ctx.dataStore.data
            .map {
                it[longKey]!!
            }
        return triggerFlow
    }

    suspend fun saveAlarmStatus(key:String,alarmStatus:Boolean){

        val booleKey = booleanPreferencesKey(key)
        ctx.dataStore.edit {
            it[booleKey] = alarmStatus
        }
    }
    fun getAlarmStatus(key: String):Flow<Boolean>{

        val booleKey = booleanPreferencesKey(key)
        val booleFlow :Flow<Boolean> = ctx.dataStore.data.map {
            it[booleKey]?:false
        }
        return booleFlow
    }
}