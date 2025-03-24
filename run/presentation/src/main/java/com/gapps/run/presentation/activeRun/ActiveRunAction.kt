package com.gapps.run.presentation.activeRun

sealed interface ActiveRunAction {
    data object OnToggleRunClick: ActiveRunAction
    data object OnFinichRunClick: ActiveRunAction
    data object OnResumeRunClick: ActiveRunAction
    data object OnBackClick: ActiveRunAction
    data class SubmitLoactionPermissionInfo(
        val acceptedLocationPermission: Boolean,
        val showLocationRationale: Boolean
    ): ActiveRunAction
    data class SubmitNotificationPermissionInfo(
        val acceptedNotificationPermission: Boolean,
        val showNotificationPermissionRationale: Boolean
    ): ActiveRunAction
    data object DismissRationaleDialog: ActiveRunAction
    class OnRunProcessed(val mapPictureBytes: ByteArray): ActiveRunAction
}