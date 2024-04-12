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

    @Delete
    suspend fun deleteNote(note: NotesModel)

    @Query("SELECT * FROM notesmodel")
    fun getNotes(): Flow<List<NotesModel>>

    @Query("SELECT * FROM notesmodel WHERE id = :id")
    suspend fun getNote(id: Int): NotesModel
}