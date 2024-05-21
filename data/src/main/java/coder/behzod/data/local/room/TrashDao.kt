package coder.behzod.data.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import coder.behzod.domain.model.TrashModel
import kotlinx.coroutines.flow.Flow

@Dao
interface TrashDao {
    @Delete
    suspend fun delete(note:TrashModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveToTrash(note: TrashModel)
    @Delete
    suspend fun deleteAll(notes:ArrayList<TrashModel>)

    @Query("SELECT *FROM trash")
    fun getTrashedNotes():Flow<List<TrashModel>>
}