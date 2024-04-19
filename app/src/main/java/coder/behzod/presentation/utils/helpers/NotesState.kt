package coder.behzod.presentation.utils.helpers

import coder.behzod.domain.utils.NoteOrder
import coder.behzod.domain.utils.OrderType

data class NotesState(
    val noteOrder: NoteOrder = NoteOrder.Date(orderType = OrderType.Descending)
)
