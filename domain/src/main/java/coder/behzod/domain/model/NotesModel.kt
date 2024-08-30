package coder.behzod.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("notes")
data class NotesModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val title: String,
    val content: String,
    val color: Int,
    val dataAdded: String,
    val alarmMapper:Int = 0,
    val alarmStatus:Boolean = false,
    val requestCode:Int,
    val stopCode:Int,
    val triggerDate:Int = 0,
    val triggerTime:Long = 0
)

@Entity("trash")
data class TrashModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val title: String,
    val content: String,
    val color: Int,
    var daysLeft: Int,
)