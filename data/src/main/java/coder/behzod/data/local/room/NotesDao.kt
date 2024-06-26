package coder.behzod.data.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import coder.behzod.domain.model.NotesModel
import coder.behzod.domain.model.TrashModel
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveNote(note: NotesModel)

    @Delete
    suspend fun deleteNote(note: NotesModel)

    @Query("SELECT * FROM notes")
    fun getNotes(): Flow<List<NotesModel>>

    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getNote(id: Int): NotesModel

    @Delete
    suspend fun deleteAll(notes:ArrayList<NotesModel>)

}