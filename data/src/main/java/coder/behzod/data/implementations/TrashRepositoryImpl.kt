package coder.behzod.data.implementations

import coder.behzod.data.local.room.TrashDao
import coder.behzod.domain.model.TrashModel
import coder.behzod.domain.repository.TrashRepository
import kotlinx.coroutines.flow.Flow

class TrashRepositoryImpl(private val trashDao: TrashDao) :TrashRepository {
    override suspend fun delete(note: TrashModel) {
        trashDao.delete(note)
    }

    override suspend fun deleteAll(notes: ArrayList<TrashModel>) {
        trashDao.deleteAll(notes)
    }

    override fun getTrashedNotes(): Flow<List<TrashModel>> = trashDao.getTrashedNotes()
}