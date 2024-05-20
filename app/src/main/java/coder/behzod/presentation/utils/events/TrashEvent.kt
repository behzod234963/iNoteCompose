package coder.behzod.presentation.utils.events

sealed class TrashEvent {
    data class IsSelected(var isSelected: Boolean = false):TrashEvent()
}