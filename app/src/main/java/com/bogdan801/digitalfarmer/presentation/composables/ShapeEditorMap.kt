package com.bogdan801.digitalfarmer.presentation.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bogdan801.digitalfarmer.R
import com.bogdan801.digitalfarmer.data.util.distanceTo
import com.bogdan801.digitalfarmer.data.util.getPolygonCenterPoint
import com.bogdan801.digitalfarmer.data.util.getZoomIndependentCircleRadius
import com.bogdan801.digitalfarmer.domain.model.Shape
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun ShapeEditorMap(
    modifier: Modifier,
    shape: Shape,
    cameraPosition: CameraPosition = CameraPosition.fromLatLngZoom(LatLng(51.7958635,33.0600247), 15f),
    onShapeChanged: (newShape: Shape) -> Unit
) {
    Column(modifier = modifier) {
        val cameraPositionState = rememberCameraPositionState {
            position = cameraPosition
        }

        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            properties = MapProperties(mapType = MapType.HYBRID),
            cameraPositionState = cameraPositionState,
            onMapLongClick = {coordinates ->
                if(!shape.isShapeClosed){
                    if(shape.isEmpty) onShapeChanged(shape.toMutableShape().setPoint(coordinates))
                    else {
                        if(shape.points[0].distanceTo(coordinates) < getZoomIndependentCircleRadius(zoom = cameraPositionState.position.zoom, scalar = 2.0)){
                            onShapeChanged(shape.toMutableShape().closeShape())
                        }
                        else onShapeChanged(shape.toMutableShape().setPoint(coordinates))
                    }
                }
            }
        ){
            if(shape.isShapeClosed) {
                Polygon(
                    points = shape.points,
                    fillColor = Color.Transparent,
                    strokeColor = MaterialTheme.colorScheme.primary,
                    strokeWidth = 10f,
                    geodesic = true
                )

                MapMarker(
                    state = MarkerState(position = getPolygonCenterPoint(shape.points)),
                    iconResourceId = R.drawable.ic_marker,
                    anchor = Offset(0.5f, 1.0f)
                )
            }
            else {
                shape.points.forEachIndexed{ _, coord ->
                    MapMarker(
                        state = MarkerState(position = coord),
                        iconResourceId = R.drawable.ic_point
                    )
                }
                Polyline(
                    points = shape.points,
                    color = MaterialTheme.colorScheme.primary,
                    width = 10f
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(text = if(shape.isShapeClosed) shape.area.toInt().toString() else "Open")

            Button(
                modifier = Modifier.width(100.dp),
                onClick = {
                    if(shape.pointCount == 1) onShapeChanged(Shape())
                    else onShapeChanged(shape.toMutableShape().removeLast())
                },
                enabled = shape.isNotEmpty
            ) {
                Text(text = "Undo")
            }

            Button(
                modifier = Modifier.width(100.dp),
                onClick = { onShapeChanged(Shape()) }
            ) {
                Text(text = "Clear")
            }
        }
    }
}