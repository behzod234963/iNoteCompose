package coder.behzod.data.implementations

import coder.behzod.data.local.room.TrashDao
import coder.behzod.domain.model.NotesModel
import coder.behzod.domain.model.TrashModel
import coder.behzod.domain.repository.TrashRepository
import kotlinx.coroutines.flow.Flow

class TrashRepositoryImpl(private val trashDao: TrashDao) :TrashRepository {

    override suspend fun saveToTrash(trashedNote: TrashModel) {
        trashDao.saveToTrash(trashedNote)
    }

    override suspend fun updateDay(id: Int, day: Int) {
        trashDao.updateDay(id,day)
    }

    override suspend fun delete(note: TrashModel) {
        trashDao.delete(note)
    }

    override suspend fun multipleDelete(notes: ArrayList<TrashModel>) {
        trashDao.multipleDelete(notes)
    }

    override fun getTrashedNotes(): Flow<List<TrashModel>> = trashDao.getTrashedNotes()

    override suspend fun restoreAll(notes: ArrayList<NotesModel>) {
        trashDao.restoreAll(notes)
    }
}