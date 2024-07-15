package coder.behzod.presentation.utils.events

sealed class AlarmEvent {
    data class StopAlarm(val isStopped:Boolean = false):AlarmEvent()
}