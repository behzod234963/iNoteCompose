package coder.behzod.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat

@Entity("notes")
data class NotesModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val title: String,
    val content: String,
    val color: Int,
    val dataAdded: String
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