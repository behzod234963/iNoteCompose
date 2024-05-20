package coder.behzod.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import coder.behzod.domain.model.NotesModel
import coder.behzod.domain.model.TrashModel

@Database(entities = [NotesModel::class,TrashModel::class], version = 1)
abstract class RoomInstance: RoomDatabase() {
    abstract val notesdDao : NotesDao
    abstract val trashDao:TrashDao
    companion object{
        const val ROOM_DB_NAME = "notes_db"
    }
}