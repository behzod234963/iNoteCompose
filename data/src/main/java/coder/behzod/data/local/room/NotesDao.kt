package coder.behzod.data.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import coder.behzod.domain.model.NotesModel
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveNote(note: NotesModel)

    @Query("UPDATE notes SET alarmStatus=:status WHERE id=:id")
    suspend fun updateStatus(id: Int,status:Boolean)

    @Query("UPDATE notes SET isRepeat=:isRepeat WHERE id=:id")
    suspend fun updateIsRepeat(id: Int,isRepeat: Boolean)

    @Query("UPDATE notes SET isFired=:isFired WHERE id=:id")
    suspend fun updateIsFired(id: Int,isFired: Boolean)

    @Query("UPDATE notes SET isScheduled=:isScheduled WHERE id=:id")
    suspend fun updateIsScheduled(id: Int, isScheduled: Boolean)

    @Delete
    suspend fun deleteNote(note: NotesModel)

    @Query("SELECT * FROM notes")
    fun getNotes(): Flow<List<NotesModel>>

    @Query("SELECT * FROM notes")
    fun getAllNotes():List<NotesModel>

    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getNote(id: Int): NotesModel

    @Delete
    suspend fun deleteAll(notes:ArrayList<NotesModel>)

}