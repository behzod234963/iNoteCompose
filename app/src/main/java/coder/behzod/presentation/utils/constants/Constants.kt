package coder.behzod.presentation.utils.constants

import androidx.compose.ui.graphics.Color
import androidx.work.Data
import androidx.work.WorkerParameters
import coder.behzod.domain.model.NotesModel

const val KEY_NOTES_DAY = "KEY_NOTES_DAY"
const val KEY_INPUT_NOTES_DAY = "KEY_INPUT_NOTES_DAY"
const val KEY_WORKER_STATUS = "KEY_WORKER_STATUS"
const val KEY_ALARM_TITLE = "KEY_ALARM_TITLE"
const val KEY_ALARM_CONTENT = "KEY_ALARM_CONTENT"
const val KEY_ALARM_STATUS = "KEY_ALARM_STATUS"
const val KEY_INT = "KEY_INT"
const val KEY_FONT_SIZE = "KEY_FONT_SIZE"
const val KEY_THEME_STATUS = "KEY_THEME_STATUS"
const val KEY_INDEX = "KEY_INDEX"
const val KEY_LIST_STATUS = "KEY_LIST_STATUS"
const val KEY_VIEW_TYPE = "KEY_VIEW_TYPE"
val notes:ArrayList<NotesModel> = ArrayList()
val colorList = listOf(
    Color.Black,
    Color.White,
    Color.Red,
    Color.Magenta,
    Color.Blue,
    Color.Cyan,
    Color.DarkGray,
    Color.Green,
    Color.Yellow
)