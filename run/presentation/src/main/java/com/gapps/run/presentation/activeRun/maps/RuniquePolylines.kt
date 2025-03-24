package com.gapps.run.presentation.activeRun.maps

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.gapps.core.domain.location.LocationTimeStamp
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Polyline

@Composable
fun RuniqeuPolylines(
    locations: List<List<LocationTimeStamp>>
) {
    val polylines = remember(locations) {
        locations.map {
            it.zipWithNext { timeStamp1, timeStamp2 ->
                PolylineUi(
                    location1 = timeStamp1.location.location,
                    location2 = timeStamp2.location.location,
                    color = PolylineColorCalculator.locationToColor(
                        location1 = timeStamp1,
                        location2 = timeStamp2
                    )
                )
            }
        }
    }

    polylines.forEach { polyline ->
        polyline.forEach { polylineUi ->
            Polyline(
                points = listOf(
                    LatLng(polylineUi.location1.lat, polylineUi.location1.long),
                    LatLng(polylineUi.location2.lat, polylineUi.location2.long),
                ),
                color = polylineUi.color,
                jointType = JointType.BEVEL
            )
        }
    }
    
}