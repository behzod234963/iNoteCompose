package coder.behzod.domain.repository

import coder.behzod.domain.model.NotesModel
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    suspend fun saveNote(note: NotesModel)

    suspend fun deleteNote(note: NotesModel)

    fun getNotes(): Flow<List<NotesModel>>

    suspend fun getNote(id: Int): NotesModel
}