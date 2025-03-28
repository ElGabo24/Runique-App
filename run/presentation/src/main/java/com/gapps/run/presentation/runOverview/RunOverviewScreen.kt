@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.gapps.run.presentation.runOverview

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gapps.core.presentation.designsystem.AnalyticsIcon
import com.gapps.core.presentation.designsystem.LogoIcon
import com.gapps.core.presentation.designsystem.LogoutIcon
import com.gapps.core.presentation.designsystem.RunIcon
import com.gapps.core.presentation.designsystem.RuniqueTheme
import com.gapps.core.presentation.designsystem.components.DropDownItem
import com.gapps.core.presentation.designsystem.components.RuniqueFloatingActionButton
import com.gapps.core.presentation.designsystem.components.RuniqueScaffold
import com.gapps.core.presentation.designsystem.components.RuniqueToolbar
import com.gapps.run.presentation.R
import com.gapps.run.presentation.runOverview.components.RunListItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun RunOverViewScreenRoot(
    onStartRunClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onAnalyticsClick: () -> Unit,
    viewModel: RunOverviewViewModel = koinViewModel(),
) {

    RunOverViewScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                RunOverviewAction.OnAnalyticsClick -> onAnalyticsClick()
                RunOverviewAction.OnStartClick -> onStartRunClick()
                RunOverviewAction.OnLogoutClick -> onLogoutClick()
                else -> Unit
            }
            viewModel.onAction(action)
        },
    )

}

@Composable
fun RunOverViewScreen(
    state: RunOverviewState,
    onAction: (RunOverviewAction) -> Unit
) {
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = topAppBarState
    )
    RuniqueScaffold(
        topAppBar = {
            RuniqueToolbar(
                showBackButton = false,
                title = stringResource(id = R.string.runique),
                scrollBehavior = scrollBehavior,
                menuItems = listOf(
                    DropDownItem(
                        icon = AnalyticsIcon,
                        title = stringResource(id = R.string.analytics)
                    ),
                    DropDownItem(
                        icon = LogoutIcon,
                        title = stringResource(id = R.string.logout)
                    )
                ),
                onMenuItemClick = { index ->
                    when (index) {
                        0 -> {
                            onAction(RunOverviewAction.OnAnalyticsClick)
                        }

                        1 -> {
                            onAction(RunOverviewAction.OnLogoutClick)
                        }
                    }
                },
                startContent = {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        imageVector = LogoIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            )
        },
        floatingActionButton = {
            RuniqueFloatingActionButton(
                icon = RunIcon,
                onClick = { onAction(RunOverviewAction.OnStartClick) })
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .padding(horizontal = 16.dp),
            contentPadding = padding,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = state.runs,
                key = { it.id }
            ){
                RunListItem(
                    modifier = Modifier.animateItemPlacement(),
                    runUi = it,
                    onDeleteClick = {
                        onAction(RunOverviewAction.DeleteRun(it))
                    }
                )
            }
            
        }
    }
}

@Preview
@Composable
private fun RunOverviewScreenPreview() {
    RuniqueTheme {
        RunOverViewScreen(
            state = RunOverviewState(),
            onAction = {}
        )
    }
}