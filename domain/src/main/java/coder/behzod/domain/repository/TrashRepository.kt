package coder.behzod.domain.repository

import coder.behzod.domain.model.NotesModel
import coder.behzod.domain.model.TrashModel
import kotlinx.coroutines.flow.Flow

interface TrashRepository {

    suspend fun saveToTrash(trashedNote:TrashModel)
    suspend fun delete(note:TrashModel)
    suspend fun updateDay(id:Int,day:Int)
    suspend fun multipleDelete(notes:ArrayList<TrashModel>)
    fun getTrashedNotes():Flow<List<TrashModel>>
    suspend fun restoreAll(notes: ArrayList<NotesModel>)
}