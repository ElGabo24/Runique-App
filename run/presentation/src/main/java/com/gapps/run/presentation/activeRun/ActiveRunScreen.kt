@file:OptIn(ExperimentalMaterial3Api::class)

package com.gapps.run.presentation.activeRun

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gapps.core.presentation.designsystem.RuniqueTheme
import com.gapps.core.presentation.designsystem.StartIcon
import com.gapps.core.presentation.designsystem.StopIcon
import com.gapps.core.presentation.designsystem.components.RuniqueActionButton
import com.gapps.core.presentation.designsystem.components.RuniqueDialog
import com.gapps.core.presentation.designsystem.components.RuniqueFloatingActionButton
import com.gapps.core.presentation.designsystem.components.RuniqueOutlinedActionButton
import com.gapps.core.presentation.designsystem.components.RuniqueScaffold
import com.gapps.core.presentation.designsystem.components.RuniqueToolbar
import com.gapps.core.presentation.ui.ObserveAsEvents
import com.gapps.run.presentation.R
import com.gapps.run.presentation.activeRun.components.RunDataCard
import com.gapps.run.presentation.activeRun.maps.TrackerMap
import com.gapps.core.notification.ActiveRunService
import com.gapps.run.presentation.util.hasLocationPermission
import com.gapps.run.presentation.util.hasNotificationPermission
import com.gapps.run.presentation.util.shouldShowLocationPermissionRationale
import com.gapps.run.presentation.util.shouldShowNotificationPermissionRationale
import org.koin.androidx.compose.koinViewModel
import java.io.ByteArrayOutputStream
import java.io.File

@Composable
fun ActiveRunScreenRoot(
    onServiceToggle: (isServiceRunning: Boolean) -> Unit,
    onFinish: () -> Unit,
    onBack: () -> Unit,
    viewModel: ActiveRunViewModel = koinViewModel()
) {
    val context = LocalContext.current
    ObserveAsEvents(flow = viewModel.events) { events ->
        when (events){
            is ActiveRunEvent.Error -> {
                Toast.makeText(context, events.error.asString(context), Toast.LENGTH_SHORT).show()
            }
            ActiveRunEvent.RunSaved -> {
                onFinish()
            }
        }
    }

    ActiveRunScreen(
        state = viewModel.state,
        onServiceToggle = onServiceToggle,
        onAction = { action ->
            when(action){
                is ActiveRunAction.OnBackClick -> {
                    if (!viewModel.state.hasStartedRunning){
                        onBack()
                    }
                }
                else -> Unit
            }

            viewModel.onAction(action)
        }
    )
}

@Composable
fun ActiveRunScreen(
    state: ActiveRunState,
    onServiceToggle: (isServiceRunning: Boolean) -> Unit,
    onAction: (ActiveRunAction) -> Unit
) {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
    ) { perms ->
        val hasCourseLocationPermission =
            perms[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        val hasFineLocationPermission =
            perms[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val hasPostNotificationPermission =
            if (Build.VERSION.SDK_INT >= 33) {
                perms[Manifest.permission.POST_NOTIFICATIONS] == true
            } else true

        val activity = context as ComponentActivity
        val showLocationRationale = activity.shouldShowLocationPermissionRationale()
        val showNotificationRationale = activity.shouldShowNotificationPermissionRationale()

        onAction(
            ActiveRunAction.SubmitLoactionPermissionInfo(
                acceptedLocationPermission = hasCourseLocationPermission && hasFineLocationPermission,
                showLocationRationale = showLocationRationale
            )
        )
        onAction(
            ActiveRunAction.SubmitNotificationPermissionInfo(
                acceptedNotificationPermission = hasPostNotificationPermission,
                showNotificationPermissionRationale = showNotificationRationale
            )
        )
    }


    LaunchedEffect(key1 = true) {
        val activity = context as ComponentActivity
        val showLocationRationale = activity.shouldShowLocationPermissionRationale()
        val showNotificationRationale = activity.shouldShowNotificationPermissionRationale()

        onAction(
            ActiveRunAction.SubmitLoactionPermissionInfo(
                acceptedLocationPermission = context.hasLocationPermission(),
                showLocationRationale = showLocationRationale
            )
        )
        onAction(
            ActiveRunAction.SubmitNotificationPermissionInfo(
                acceptedNotificationPermission = context.hasNotificationPermission(),
                showNotificationPermissionRationale = showNotificationRationale
            )
        )

        if (!showLocationRationale && !showNotificationRationale) {
            permissionLauncher.requestRuniquePermissions(context)
        }
    }

    LaunchedEffect(key1 = state.isRunFinish) {
        if (state.isRunFinish) {
            onServiceToggle(false)
        }
    }

    val isServiceActive by ActiveRunService.isServiceActive.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = state.shouldTrack, isServiceActive) {
        if (context.hasLocationPermission() && state.shouldTrack && !isServiceActive) {
            onServiceToggle(true)
        }
    }

    RuniqueScaffold(
        withGradient = false,
        topAppBar = {
            RuniqueToolbar(
                showBackButton = true,
                title = stringResource(id = R.string.active_run),
                onBackClick = {
                    onAction(ActiveRunAction.OnBackClick)
                }
            )
        },
        floatingActionButton = {
            RuniqueFloatingActionButton(
                icon = if (state.shouldTrack) {
                    StopIcon
                } else {
                    StartIcon
                },
                onClick = { onAction(ActiveRunAction.OnToggleRunClick) },
                iconSize = 20.dp,
                contentDescription = if (state.shouldTrack) {
                    stringResource(id = R.string.pause_run)
                } else {
                    stringResource(id = R.string.start_run)
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            TrackerMap(
                modifier = Modifier.fillMaxSize(),
                isRunFinished = state.isRunFinish,
                currentLocation = state.currentLocation,
                locations = state.runData.locations,
                onSnapshot = { bmp ->
                    val stream = ByteArrayOutputStream()
                    stream.use {
                        bmp.compress(
                            Bitmap.CompressFormat.JPEG,
                            80,
                            it
                        )
                    }
                    onAction(ActiveRunAction.OnRunProcessed(stream.toByteArray()))
                }
            )
            RunDataCard(
                modifier = Modifier
                    .padding(16.dp)
                    .padding(padding)
                    .fillMaxWidth(),
                elapsedTime = state.elapsedTime,
                runData = state.runData
            )
        }
    }

    if (!state.shouldTrack && state.hasStartedRunning) {
        RuniqueDialog(
            title = stringResource(id = R.string.running_is_paused),
            onDismiss = {
                onAction(ActiveRunAction.OnResumeRunClick)
            },
            description = stringResource(id = R.string.resume_or_finish_run),
            primaryButton = {
                RuniqueActionButton(
                    modifier = Modifier.weight(1f),
                    text = stringResource(id = R.string.resume),
                    isLoading = false,
                    onClick = {
                        onAction(ActiveRunAction.OnResumeRunClick)
                    }
                )
            },
            secondaryButton = {
                RuniqueOutlinedActionButton(
                    modifier = Modifier.weight(1f),
                    text = stringResource(id = R.string.finish),
                    isLoading = state.isSavingRun,
                    onClick = {
                        onAction(ActiveRunAction.OnFinichRunClick)
                    }
                )
            }
        )
    }

    if (state.showLocationRationale || state.showNotificationRationale) {
        RuniqueDialog(
            title = stringResource(id = R.string.permission_required),
            onDismiss = { /*Normal dismissing not allowed for permissions*/ },
            description = when {
                state.showNotificationRationale && state.showLocationRationale -> {
                    stringResource(id = R.string.location_notification_rationale)
                }

                state.showNotificationRationale -> {
                    stringResource(id = R.string.location_rationale)
                }

                else -> {
                    stringResource(id = R.string.notification_rationale)
                }
            },
            primaryButton = {
                RuniqueOutlinedActionButton(
                    text = stringResource(id = R.string.okey),
                    isLoading = false,
                    onClick = {
                        onAction(ActiveRunAction.DismissRationaleDialog)
                        permissionLauncher.requestRuniquePermissions(context)
                    }
                )
            })
    }
}

private fun ActivityResultLauncher<Array<String>>.requestRuniquePermissions(
    context: Context
) {
    val hasLocationPermission = context.hasLocationPermission()
    val hasNotificationPermission = context.hasNotificationPermission()

    val locationPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
    )
    val notificationPermissions = if (Build.VERSION.SDK_INT >= 33)
        arrayOf(Manifest.permission.POST_NOTIFICATIONS) else arrayOf()

    when {
        !hasLocationPermission && !hasNotificationPermission -> {
            launch(locationPermissions + notificationPermissions)
        }

        !hasLocationPermission -> {
            launch(locationPermissions)
        }

        !hasNotificationPermission -> {
            launch(notificationPermissions)
        }
    }
}

@Preview
@Composable
private fun ActiveRunScreenPreview() {
    RuniqueTheme {
        ActiveRunScreen(
            state = ActiveRunState(),
            onServiceToggle = {},
            onAction = {}
        )
    }
}

