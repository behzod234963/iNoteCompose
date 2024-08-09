package coder.behzod.presentation.utils.constants

import androidx.compose.ui.graphics.Color
import coder.behzod.domain.model.NotesModel
import coder.behzod.presentation.theme.blue
import coder.behzod.presentation.theme.green
import coder.behzod.presentation.theme.cyan
import coder.behzod.presentation.theme.red
import coder.behzod.presentation.theme.yellow


val noteModel = NotesModel(
    id = -1,
    title = "",
    content = "",
    dataAdded = "",
    color = 0
)
const val KEY_ALARM_DATE_AND_TIME = "KEY_ALARM_DATE_AND_TIME"
const val KEY_INIT_MODEL_ID = "KEY_INIT_MODEL_ID"
const val KEY_ALARM_STATUS = "KEY_ALARM_STATUS"
const val KEY_INT = "KEY_INT"
const val KEY_LOCAL_YEAR = "KEY_LOCAL_YEAR"
const val KEY_LOCAL_MONTH = "KEY_LOCAL_MONTH"
const val KEY_LOCAL_DAY = "KEY_LOCAL_DAY"
const val KEY_FONT_SIZE = "KEY_FONT_SIZE"
const val KEY_THEME_STATUS = "KEY_THEME_STATUS"
const val KEY_INDEX = "KEY_INDEX"
const val KEY_LIST_STATUS = "KEY_LIST_STATUS"
const val KEY_VIEW_TYPE = "KEY_VIEW_TYPE"
val notes:ArrayList<NotesModel> = ArrayList()
val colorsList = listOf(
    Color.White,
    yellow,
    green,
    cyan,
    red,
    blue
)