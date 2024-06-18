package coder.behzod.presentation.utils.helpers

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import coder.behzod.R
import coder.behzod.presentation.utils.events.NewNoteEvent
import coder.behzod.presentation.utils.events.NotesEvent

class ShareNote {

    fun execute(
        title:String,
        content:String,
        ctx: Context,
        onClick:()->Unit
    ){
        Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT,"$title\n$content")
        }.also {
            val chooser = Intent.createChooser(
                it,ctx.getString(R.string.share_via)
            )
            startActivity(ctx,chooser,null)
        }
        onClick()
    }
}