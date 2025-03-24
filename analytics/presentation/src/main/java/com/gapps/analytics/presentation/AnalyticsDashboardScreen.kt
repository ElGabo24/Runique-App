@file:OptIn(ExperimentalMaterial3Api::class)

package com.gapps.analytics.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gapps.analytics.presentation.components.AnalyticsCard
import com.gapps.core.presentation.designsystem.RuniqueTheme
import com.gapps.core.presentation.designsystem.components.RuniqueScaffold
import com.gapps.core.presentation.designsystem.components.RuniqueToolbar
import org.koin.androidx.compose.koinViewModel

@Composable
fun AnalyticsDashboardScreenRoot(
    onBackClick: () -> Unit,
    viewModel: AnalyticsDashboardViewModel = koinViewModel()
) {

    AnalyticsDashboardScreen(
        state = viewModel.state,
        onAction = {
            when (it) {
                AnalyticsDashboardAction.OnBackClick -> onBackClick()
            }
        }
    )
}

@Composable
fun AnalyticsDashboardScreen(
    state: AnalyticsDashboardState?,
    onAction: (AnalyticsDashboardAction) -> Unit
) {

    RuniqueScaffold(
        topAppBar = {
            RuniqueToolbar(
                showBackButton = true,
                title = stringResource(R.string.analytics),
                onBackClick = {
                    onAction(AnalyticsDashboardAction.OnBackClick)
                }
            )
        }
    ) { padding ->
        if (state == null){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    AnalyticsCard(
                        modifier = Modifier.weight(1f),
                        title = stringResource(id = R.string.total_distance_run),
                        value = state.totalDistanceRun
                    )
                    Spacer(modifier = Modifier.padding(16.dp))
                    AnalyticsCard(
                        modifier = Modifier.weight(1f),
                        title = stringResource(id = R.string.total_time_run),
                        value = state.totalTimeRun
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    AnalyticsCard(
                        modifier = Modifier.weight(1f),
                        title = stringResource(id = R.string.fastest_ever_run),
                        value = state.fastestEverRun
                    )
                    Spacer(modifier = Modifier.padding(16.dp))
                    AnalyticsCard(
                        modifier = Modifier.weight(1f),
                        title = stringResource(id = R.string.highest_heart_rate),
                        value = "0 bmp"
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    AnalyticsCard(
                        modifier = Modifier.weight(1f),
                        title = stringResource(id = R.string.avg_distance_per_run),
                        value = state.avgDistance
                    )
                    Spacer(modifier = Modifier.padding(16.dp))
                    AnalyticsCard(
                        modifier = Modifier.weight(1f),
                        title = stringResource(id = R.string.avg_pace_per_run),
                        value = state.avgPace
                    )
                }
            }
        }
    }
    
}

@Preview
@Composable
private fun AnalyticsDashboardScreenPreview() {
    RuniqueTheme {
        AnalyticsDashboardScreen(
            state = AnalyticsDashboardState(
                totalDistanceRun = "0.2 km",
                totalTimeRun = "0d 0h 0m",
                fastestEverRun = "143.3 km/h",
                avgDistance = "0.1 km",
                avgPace = "07:30"
            ),
            onAction = {}
        )
    }
}