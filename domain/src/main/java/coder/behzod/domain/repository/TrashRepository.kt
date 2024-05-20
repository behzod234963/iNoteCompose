package coder.behzod.domain.repository

import coder.behzod.domain.model.TrashModel
import kotlinx.coroutines.flow.Flow

interface TrashRepository {

    suspend fun delete(note:TrashModel)
    suspend fun deleteAll(notes:ArrayList<TrashModel>)
    fun getTrashedNotes():Flow<List<TrashModel>>
}