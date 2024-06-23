package coder.behzod.data.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import coder.behzod.domain.model.NotesModel
import coder.behzod.domain.model.TrashModel
import kotlinx.coroutines.flow.Flow

@Dao
interface TrashDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveToTrash(trashedNote: TrashModel)

    @Delete
    suspend fun delete(note:TrashModel)

    @Delete
    suspend fun multipleDelete(notes: ArrayList<TrashModel>)

    @Query("SELECT *FROM trash")
    fun getTrashedNotes():Flow<List<TrashModel>>

    @Query("SELECT * FROM trash")
    fun getListOfNotes():List<TrashModel>

    @Insert
    suspend fun restoreAll(notes:ArrayList<NotesModel>)


    @Query("UPDATE trash SET daysLeft=:daysLeft WHERE id=:id")
    suspend fun updateDay(id:Int,daysLeft:Int)
}