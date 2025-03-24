package com.gapps.run.presentation.runOverview

import com.gapps.run.presentation.runOverview.model.RunUi

data class RunOverviewState(
    val runs: List<RunUi> = emptyList()
)
