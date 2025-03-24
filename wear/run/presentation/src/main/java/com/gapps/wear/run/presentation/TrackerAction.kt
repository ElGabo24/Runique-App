package com.gapps.wear.run.presentation

sealed interface TrackerAction {
    data object OnToggleRunClick : TrackerAction
    data object OnFinishRunClick : TrackerAction
    data class OnBodyPermissionResult(val isGranted: Boolean) : TrackerAction
    data class OnEnterAmbientMode(val burnInProtectionRequired: Boolean) : TrackerAction
    data object OnExitAmbientMode : TrackerAction
}