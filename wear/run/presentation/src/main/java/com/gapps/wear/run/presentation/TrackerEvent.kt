package com.gapps.wear.run.presentation

import com.gapps.core.presentation.ui.UiText

sealed interface TrackerEvent {
    data object RunFinish: TrackerEvent
    data class Error(val message: UiText): TrackerEvent
}