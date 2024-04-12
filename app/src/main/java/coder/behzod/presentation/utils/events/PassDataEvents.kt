package coder.behzod.presentation.utils.events

sealed class PassDataEvents {
    data class PassStatus(val status: Boolean) : PassDataEvents()
}