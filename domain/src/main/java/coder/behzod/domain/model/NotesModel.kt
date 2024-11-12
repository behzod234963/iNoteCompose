package coder.behzod.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("notes")
data class NotesModel(
    @PrimaryKey
    val id: Int = generateID(),
    val title: String,
    val content: String,
    val color: Int,
    val dataAdded: String,
    val alarmMapper: Int = 0,
    val alarmStatus: Boolean = false,
    val alarmDate: String,
    val alarmTime: String,
    val triggerDate: Int = 0,
    val triggerTime: Long = 0,
    val isRepeat: Boolean,
    val isFired: Boolean = false,
    val isScheduled: Boolean = false,
) {
    companion object {
        fun generateID(): Int {
            val range = 0..9999
            return range.random()
        }
    }
}

@Entity("trash")
data class TrashModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = generateTrashID(),
    val title: String,
    val content: String,
    val color: Int,
    var daysLeft: Int = 10,
) {
    companion object {
        fun generateTrashID(): Int {
            val range = 0..9999
            return range.random()
        }
    }
}