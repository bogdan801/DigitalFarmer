package com.bogdan801.digitalfarmer.presentation.composables

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import com.bogdan801.digitalfarmer.R
import com.bogdan801.digitalfarmer.data.util.readBitmapFromPrivateStorage
import com.bogdan801.digitalfarmer.data.util.saveBitmapToPrivateStorage
import com.bogdan801.digitalfarmer.domain.model.Field
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.*
import java.io.File

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun FieldSnapshot(
    modifier: Modifier = Modifier,
    field: Field,
    shouldSaveSnapshot: Boolean = true,
    onMapStartedLoading: () -> Unit = {},
    onSnapshotGenerated: (snapshot: Bitmap?) -> Unit = {}
) {
    val context = LocalContext.current
    var isPreviewSaved by remember { mutableStateOf(File(context.filesDir, field.id).exists()) }
    if (!isPreviewSaved){
        var isMapLoaded by remember { mutableStateOf(false) }
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(
                field.shape.center,
                15f
            )
        }
        var isMapShown by remember{ mutableStateOf(false) }
        GoogleMap(
            modifier = modifier.onGloballyPositioned { coords ->
                if(coords.size.width > 0) isMapShown = true
            },
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
            if(field.shape.isShapeClosed) {
                Polygon(
                    points = field.shape.points,
                    fillColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                    strokeColor = MaterialTheme.colorScheme.primary,
                    strokeWidth = 10f,
                    geodesic = true
                )
                if(field.plantedCrop != null){
                    MapMarker(
                        state = rememberMarkerState(position = field.shape.center),
                        iconResourceId = R.drawable.ic_circle,
                        tint = MaterialTheme.colorScheme.primary,
                        scale = 3f
                    )
                    MapMarker(
                        state = rememberMarkerState(position = field.shape.center),
                        iconResourceId = field.plantedCrop.drawableID,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        scale = 2f
                    )
                }
            }
            else {
                Polyline(
                    points = field.shape.points,
                    color = MaterialTheme.colorScheme.primary,
                    width = 10f
                )
            }

            if(isMapShown){
                MapEffect(key1 = true){ map ->
                    map.moveCamera(CameraUpdateFactory.newLatLngBounds(field.shape.bounds,50))
                    onMapStartedLoading()
                }

                MapEffect(key1 = isMapLoaded) { map ->
                    if(shouldSaveSnapshot && isMapLoaded){
                        map.snapshot { snapshot ->
                            if(snapshot!=null) {
                                saveBitmapToPrivateStorage(context, snapshot, field.id)
                                isPreviewSaved = true
                                onSnapshotGenerated(readBitmapFromPrivateStorage(context, field.id))
                            }
                        }
                    }
                }
            }
        }
    }
    else {
        val preview = remember { mutableStateOf(readBitmapFromPrivateStorage(context, field.id)) }
        preview.value?.let {
            Image(
                modifier = modifier,
                bitmap = it.asImageBitmap(),
                contentDescription = "Preview of the field",
                contentScale = ContentScale.Crop
            )
        }
        DisposableEffect(key1 = preview.value){
            onDispose {
                preview.value?.recycle()
            }
        }
    }
}
