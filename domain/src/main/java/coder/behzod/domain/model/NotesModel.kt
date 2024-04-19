package coder.behzod.domain.model

import android.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NotesModel(
    @PrimaryKey(autoGenerate = true)
    val id:Int? = null,
    val title:String? = null,
    val note:String,
    val color:Int,
    val dataAdded:String
)
