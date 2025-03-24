package com.gapps.run.presentation.activeRun

import com.gapps.core.presentation.ui.UiText

sealed interface ActiveRunEvent  {
    data class Error(val error: UiText): ActiveRunEvent
    data object RunSaved: ActiveRunEvent
}