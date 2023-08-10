package com.bogdan801.digitalfarmer.presentation.composables

import android.util.Size
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bogdan801.digitalfarmer.data.util.getBoundsZoomLevel
import com.bogdan801.digitalfarmer.data.util.toFormattedDateString
import com.bogdan801.digitalfarmer.domain.model.Field
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlin.math.roundToInt

@Composable
fun FieldCard(
    modifier: Modifier = Modifier,
    field: Field,
    onDeleteClickButton: () -> Unit = {},
    onEditClickButton: () -> Unit = {}
) {
    val density = LocalDensity.current
    BoxWithConstraints(modifier = modifier) {
        val widthInPx = with(density){maxWidth.toPx()}.roundToInt()
        val heightInPx = with(density){maxHeight.toPx()}.roundToInt()
        Card(modifier = Modifier.fillMaxSize()) {
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(
                    field.shape.center,
                    getBoundsZoomLevel(field.shape.bounds, Size(widthInPx, heightInPx)) - 3f
                )
            }

            GoogleMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(mapType = MapType.HYBRID),
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = false,
                    zoomGesturesEnabled = false,
                    tiltGesturesEnabled = false,
                    scrollGesturesEnabled = false,
                    rotationGesturesEnabled = false,
                    mapToolbarEnabled = false,
                    scrollGesturesEnabledDuringRotateOrZoom = false
                )
            ){
                if(field.shape.isShapeClosed) {
                    Polygon(
                        points = field.shape.points,
                        fillColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                        strokeColor = MaterialTheme.colorScheme.primary,
                        strokeWidth = 10f,
                        geodesic = true
                    )
                }
                else {
                    Polyline(
                        points = field.shape.points,
                        color = MaterialTheme.colorScheme.primary,
                        width = 10f
                    )
                }
            }
            Column(modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
            ) {
                Text(text = field.name, fontSize = 24.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = field.plantedCrop.toString())
                Text(text = field.plantDate?.toFormattedDateString() ?: "Not planted yet")
                Text(text = field.harvestDate?.toFormattedDateString() ?: "Not planted yet")
            }
        }
    }



}