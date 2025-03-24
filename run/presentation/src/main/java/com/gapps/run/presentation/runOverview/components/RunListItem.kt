@file:OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalLayoutApi::class
)

package com.gapps.run.presentation.runOverview.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.gapps.core.domain.location.Location
import com.gapps.core.domain.run.Run
import com.gapps.core.presentation.designsystem.CalendarIcon
import com.gapps.core.presentation.designsystem.RunOutlinedIcon
import com.gapps.core.presentation.designsystem.RuniqueTheme
import com.gapps.run.presentation.R
import com.gapps.run.presentation.runOverview.mapper.toRunUi
import com.gapps.run.presentation.runOverview.model.RunDataUi
import com.gapps.run.presentation.runOverview.model.RunUi
import java.time.ZonedDateTime
import kotlin.math.max
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@Composable
fun RunListItem(
    modifier: Modifier = Modifier, runUi: RunUi, onDeleteClick: () -> Unit
) {
    var showDropDown by remember { mutableStateOf(false) }
    Box {
        Column(
            modifier = modifier
                .clip(RoundedCornerShape(15.dp))
                .background(MaterialTheme.colorScheme.surface)
                .combinedClickable(onClick = { }, onLongClick = { showDropDown = true })
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MapImage(imageUrl = runUi.mapPictureUrl)
            RunningTimeSection(
                modifier = Modifier.fillMaxWidth(), duration = runUi.duration
            )
            HorizontalDivider(
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
            )
            RunningDateSection(
                dateTime = runUi.datetime
            )
            DataGrid(
                modifier = Modifier.fillMaxWidth(), runUi = runUi
            )
        }
        DropdownMenu(expanded = showDropDown, onDismissRequest = { showDropDown = false }) {
            DropdownMenuItem(text = {
                Text(text = stringResource(id = R.string.delete))
            }, onClick = {
                showDropDown = false
                onDeleteClick()
            })
        }
    }
}

@Composable
fun MapImage(
    modifier: Modifier = Modifier, imageUrl: String?
) {
    SubcomposeAsyncImage(modifier = modifier
        .fillMaxWidth()
        .aspectRatio(16 / 9f)
        .clip(RoundedCornerShape(15.dp)),
        model = imageUrl,
        contentDescription = stringResource(R.string.run_map),
        loading = {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        error = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.errorContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.error_couldnt_load_image),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

    )
}

@Composable
private fun RunningTimeSection(
    modifier: Modifier = Modifier, duration: String
) {
    Row(
        modifier = modifier, verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(4.dp), contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = RunOutlinedIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )

        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.total_running_time),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = duration,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }

    }
}

@Composable
private fun RunningDateSection(
    modifier: Modifier = Modifier, dateTime: String
) {
    Row(
        modifier = modifier, verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = CalendarIcon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = dateTime, color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun DataGrid(
    modifier: Modifier = Modifier, runUi: RunUi
) {
    val runDataUiList = listOf(
        RunDataUi(
            name = stringResource(id = R.string.distance), value = runUi.distance
        ),
        RunDataUi(
            name = stringResource(id = R.string.pace), value = runUi.pace
        ),
        RunDataUi(
            name = stringResource(id = R.string.avg_speed), value = runUi.avgSpeed
        ),
        RunDataUi(
            name = stringResource(id = R.string.max_speed), value = runUi.maxSpeed
        ),
        RunDataUi(
            name = stringResource(id = R.string.total_elevation), value = runUi.totalElevation
        ),
        RunDataUi(
            name = stringResource(id = R.string.avg_heart_rate), value = runUi.avgHeartRate
        ),
        RunDataUi(
            name = stringResource(id = R.string.max_heart_rate), value = runUi.maxHeartRate
        ),
    )

    var maxWidth by remember { mutableIntStateOf(0) }
    val maxWidthDp = with(LocalDensity.current) { maxWidth.toDp() }

    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        runDataUiList.forEach { run ->
            DataGrdCell(
                runData = run,
                modifier = Modifier
                    .defaultMinSize(minWidth = maxWidthDp)
                    .onSizeChanged {
                        maxWidth = max(maxWidth, it.width)
                    }
            )
        }
    }

}

@Composable
private fun DataGrdCell(
    runData: RunDataUi,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = runData.name,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 12.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = runData.value,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Preview
@Composable
private fun RunListItemPreview() {
    RuniqueTheme {
        RunListItem(runUi = Run(
            id = "123",
            duration = 10.minutes + 30.seconds,
            dateTimeUtc = ZonedDateTime.now(),
            distanceMeters = 2543,
            location = Location(0.0, 0.0),
            maxSpeedKmh = 15.6334,
            totalElevationMeters = 123,
            mapPictureUrl = null,
            avgHeartRate = 120,
            maxHeartRate = 180
        ).toRunUi(), onDeleteClick = {})
    }
}