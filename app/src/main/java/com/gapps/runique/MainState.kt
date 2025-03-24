package com.gapps.runique

data class MainState(
    val isLoggedIn: Boolean = false,
    val isCheckingAuth: Boolean = false,
    val showAnalyticssInstallDialog: Boolean = false,
)
