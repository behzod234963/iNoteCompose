package coder.behzod.domain.repository

import coder.behzod.domain.model.NotesModel
import kotlinx.coroutines.flow.Flow

interface NotesRepository {

    suspend fun saveNote(note: NotesModel)
    suspend fun deleteNote(note: NotesModel)
    fun getNotes(): Flow<List<NotesModel>>
    suspend fun getNote(id: Int): NotesModel
    suspend fun deleteAll(notes: ArrayList<NotesModel>)
    fun getAllNotes():List<NotesModel>
    suspend fun updateStatus(requestCode: Int, status:Boolean)
    suspend fun updateIsRepeat(requestCode: Int,isRepeat:Boolean)
    suspend fun updateIsFired(requestCode: Int,isFired:Boolean)
}