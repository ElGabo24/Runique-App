package com.gapps.analytics.presentation

sealed interface AnalyticsDashboardAction {
    data object OnBackClick: AnalyticsDashboardAction
}