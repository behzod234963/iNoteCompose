package coder.behzod.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import coder.behzod.domain.model.NotesModel

@Database(entities = [NotesModel::class], version = 1)
abstract class RoomInstance: RoomDatabase() {
    abstract val dao : NotesDao
    companion object{
        const val ROOM_DB_NAME = "notes_db"
    }
}