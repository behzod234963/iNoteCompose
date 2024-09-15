package coder.behzod.data.implementations

import coder.behzod.data.local.room.NotesDao
import coder.behzod.domain.model.NotesModel
import coder.behzod.domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow

class NoteRepositoryImpl(private val dao: NotesDao) : NotesRepository {
    override suspend fun saveNote(note: NotesModel) {
        dao.saveNote(note)
    }

    override suspend fun deleteNote(note: NotesModel) {
        dao.deleteNote(note)
    }

    override fun getNotes(): Flow<List<NotesModel>> = dao.getNotes()

    override suspend fun getNote(id: Int): NotesModel = dao.getNote(id)
    override suspend fun deleteAll(notes: ArrayList<NotesModel>) {
        dao.deleteAll(notes)
    }

    override fun getAllNotes(): List<NotesModel> = dao.getAllNotes()

    override suspend fun updateStatus(requestCode: Int,status: Boolean) {
        dao.updateStatus(requestCode = requestCode, status)
    }

    override suspend fun updateIsRepeat(requestCode: Int, isRepeat: Boolean) {
        dao.updateIsRepeat(requestCode,isRepeat)
    }

    override suspend fun updateIsFired(requestCode: Int, isFired: Boolean) {
        dao.updateIsFired(requestCode,isFired)
    }
}