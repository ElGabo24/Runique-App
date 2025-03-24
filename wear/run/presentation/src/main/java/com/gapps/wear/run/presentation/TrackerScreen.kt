package com.gapps.wear.run.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.material3.FilledTonalIconButton
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.IconButtonDefaults
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.OutlinedIconButton
import androidx.wear.compose.material3.Text
import androidx.wear.compose.ui.tooling.preview.WearPreviewDevices
import com.gapps.core.notification.ActiveRunService
import com.gapps.core.presentation.designsystem.ExclamationMarkIcon
import com.gapps.core.presentation.designsystem.FinishIcon
import com.gapps.core.presentation.designsystem.PauseIcon
import com.gapps.core.presentation.designsystem.StartIcon
import com.gapps.core.presentation.designsystem_wear.RuniqueTheme
import com.gapps.core.presentation.ui.ObserveAsEvents
import com.gapps.core.presentation.ui.formatted
import com.gapps.core.presentation.ui.toFormattedHeartRate
import com.gapps.core.presentation.ui.toFormattedKm
import com.gapps.wear.run.presentation.ambient.AmbientObserver
import com.gapps.wear.run.presentation.ambient.ambientMode
import com.gapps.wear.run.presentation.components.RunDataCard
import org.koin.androidx.compose.koinViewModel

@Composable
fun TrackerScreenRoot(
    onServiceToggle: (isServiceRunning: Boolean) -> Unit,
    viewModel: TrackerViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val state = viewModel.state
    val isServiceActive by ActiveRunService.isServiceActive.collectAsStateWithLifecycle()
    LaunchedEffect(state.isRunActive, state.hasStartedRunning, isServiceActive) {
        if (state.isRunActive && !isServiceActive){
            onServiceToggle(true)
        }
    }
    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is TrackerEvent.Error -> Toast.makeText(
                context,
                event.message.asString(context),
                Toast.LENGTH_LONG
            ).show()

            TrackerEvent.RunFinish -> {
                onServiceToggle(false)
            }
        }
    }

    TrackerScreen(
        state = viewModel.state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun TrackerScreen(
    state: TrackerState,
    onAction: (TrackerAction) -> Unit
) {
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
    ) { perms ->
        val hasBodySensorPermission = perms[Manifest.permission.BODY_SENSORS] == true
        onAction(TrackerAction.OnBodyPermissionResult(hasBodySensorPermission))
    }

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        val hasBodySensorPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.BODY_SENSORS
        ) == PackageManager.PERMISSION_GRANTED
        onAction(TrackerAction.OnBodyPermissionResult(hasBodySensorPermission))

        val hasNotificationPermission = if (Build.VERSION.SDK_INT >= 33) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else true

        val permissions = mutableListOf<String>()
        if (!hasBodySensorPermission) {
            permissions.add(Manifest.permission.BODY_SENSORS)
        }
        if (!hasNotificationPermission && Build.VERSION.SDK_INT >= 33) {
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        permissionLauncher.launch(permissions.toTypedArray())
    }
    
    AmbientObserver(
        onEnterAmbient = { onAction(TrackerAction.OnEnterAmbientMode(it.burnInProtectionRequired)) },
        onExitAmbient = { onAction(TrackerAction.OnExitAmbientMode) }
    )

    if (state.isConnectedPhoneNearby) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .ambientMode(state.isAmbientMode, state.burnInProtctionRequired),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                RunDataCard(
                    modifier = Modifier.weight(1f),
                    title = stringResource(id = R.string.heart_rate),
                    value = if (state.canTrackHeartRate) {
                        state.heartRate.toFormattedHeartRate()
                    } else {
                        stringResource(id = R.string.unsopported)
                    },
                    valueTextColor = if (state.canTrackHeartRate) {
                        MaterialTheme.colorScheme.onSurface
                    } else {
                        MaterialTheme.colorScheme.error
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                RunDataCard(
                    modifier = Modifier.weight(1f),
                    title = stringResource(id = R.string.distance),
                    value = (state.distanceMeter / 1000.0).toFormattedKm()
                )
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary),
                text = state.elapsedDuration.formatted(),
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimary,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (state.isTrackable) {
                    ToggleRunButton(
                        isRunActive = state.isRunActive,
                        onClick = {
                            onAction(TrackerAction.OnToggleRunClick)
                        }
                    )
                    if (!state.isRunActive && state.hasStartedRunning) {
                        FilledTonalIconButton(
                            onClick = { onAction(TrackerAction.OnFinishRunClick) },
                            colors = IconButtonDefaults.filledTonalIconButtonColors(
                                contentColor = MaterialTheme.colorScheme.onBackground
                            )
                        ) {
                            Icon(
                                imageVector = FinishIcon,
                                contentDescription = stringResource(id = R.string.finish_run)
                            )
                        }
                    }
                } else {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        text = stringResource(id = R.string.open_active_run_screen),
                        textAlign = TextAlign.Center
                    )
                }

            }

        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = ExclamationMarkIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(id = R.string.connect_your_phone),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )

        }
    }
}

@Composable
fun ToggleRunButton(
    modifier: Modifier = Modifier,
    isRunActive: Boolean,
    onClick: () -> Unit
) {
    OutlinedIconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        if (isRunActive) {
            Icon(
                imageVector = PauseIcon,
                contentDescription = stringResource(id = R.string.pause_run),
                tint = MaterialTheme.colorScheme.onBackground
            )
        } else {
            Icon(
                imageVector = StartIcon,
                contentDescription = stringResource(id = R.string.start_run),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@WearPreviewDevices
@Composable
private fun TrackerScreenPreview() {
    RuniqueTheme {
        TrackerScreen(
            state = TrackerState(
                isConnectedPhoneNearby = true,
                isRunActive = false,
                isTrackable = true,
                hasStartedRunning = false,
                canTrackHeartRate = true,
                distanceMeter = 1000,
                heartRate = 100
            ),
            onAction = {}
        )
    }
}