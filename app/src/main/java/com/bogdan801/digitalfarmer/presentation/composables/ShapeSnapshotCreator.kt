package com.bogdan801.digitalfarmer.presentation.composables

import android.graphics.Bitmap
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.bogdan801.digitalfarmer.domain.model.Shape
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.*


@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun ShapeSnapshotCreator(
    modifier: Modifier = Modifier,
    shape: Shape,
    zoom: Float,
    onSnapShotGenerated: (mapImage: Bitmap) -> Unit = {}
) {
    var isMapLoaded by remember { mutableStateOf(false) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            shape.center,
            zoom
        )
    }

    GoogleMap(
        modifier = modifier,
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
        ),
        onMapLoaded = {
            isMapLoaded = true
        }
    ){
        if(shape.isShapeClosed) {
            Polygon(
                points = shape.points,
                fillColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                strokeColor = MaterialTheme.colorScheme.primary,
                strokeWidth = 10f,
                geodesic = true
            )
        }
        else {
            Polyline(
                points = shape.points,
                color = MaterialTheme.colorScheme.primary,
                width = 10f
            )
        }
        MapEffect(key1 = isMapLoaded) { map ->
            map.snapshot { snapshot ->
                if(snapshot!=null && isMapLoaded) onSnapShotGenerated(snapshot)
            }
        }
    }
}
