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
    override suspend fun updateIsScheduled(id: Int, isScheduled: Boolean) = dao.updateIsScheduled(id,isScheduled)

    override suspend fun updateStatus(id: Int, status: Boolean) {
        dao.updateStatus(id, status)
    }

    override suspend fun updateIsRepeat(id: Int, isRepeat: Boolean) {
        dao.updateIsRepeat(id,isRepeat)
    }

    override suspend fun updateIsFired(id: Int, isFired: Boolean) {
        dao.updateIsScheduled(id,isFired)
    }
}