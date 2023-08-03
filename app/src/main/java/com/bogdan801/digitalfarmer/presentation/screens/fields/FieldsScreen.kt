package com.bogdan801.digitalfarmer.presentation.screens.fields

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.bogdan801.digitalfarmer.R
import com.bogdan801.digitalfarmer.data.util.distanceTo
import com.bogdan801.digitalfarmer.data.util.getZoomIndependentCircleRadius
import com.bogdan801.digitalfarmer.presentation.composables.MapMarker
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.google.maps.android.SphericalUtil


@Composable
fun FieldsScreen(
    navController: NavHostController,
    viewModel: FieldsViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val userData = remember { viewModel.authUIClient.getSignedInUser() }
    val state by viewModel.screenState.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(LatLng(51.7958635,33.0600247), 15f)
        }



        val isShapeClosed by remember {
            derivedStateOf {
                if(viewModel.shape.size >= 3) viewModel.shape[0] == viewModel.shape.last()
                else false
            }
        }

        val area by remember { derivedStateOf { SphericalUtil.computeArea(viewModel.shape).toInt() } }

        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            properties = MapProperties(mapType = MapType.HYBRID),
            cameraPositionState = cameraPositionState,
            onMapLongClick = {
                if(!isShapeClosed){
                    viewModel.shape = if(viewModel.shape.isEmpty()) viewModel.shape + listOf(it)
                    else {
                        if(viewModel.shape[0].distanceTo(it) < getZoomIndependentCircleRadius(zoom = cameraPositionState.position.zoom, scalar = 2.0)){
                            viewModel.shape + viewModel.shape[0]
                        }
                        else viewModel.shape + listOf(it)
                    }
                }
            }
        ){
            if(isShapeClosed) {
                Polygon(
                    points = viewModel.shape,
                    fillColor = Color.Transparent,
                    strokeColor = MaterialTheme.colorScheme.primary,
                    strokeWidth = 10f,
                    geodesic = true
                )
            }
            else {
                viewModel.shape.forEachIndexed{ _, coord ->
                    MapMarker(
                        state = MarkerState(position = coord),
                        iconResourceId = R.drawable.ic_marker
                    )
                }
                Polyline(
                    points = viewModel.shape,
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
            Text(text = if(isShapeClosed) area.toString() else "Open")

            Button(
                modifier = Modifier.width(100.dp),
                onClick = {
                    viewModel.shape = if(viewModel.shape.size == 1) listOf()
                            else viewModel.shape.subList(0, viewModel.shape.lastIndex)
                },
                enabled = viewModel.shape.isNotEmpty()
            ) {
                Text(text = "Undo")
            }

            Button(
                modifier = Modifier.width(100.dp),
                onClick = { viewModel.shape = listOf() }
            ) {
                Text(text = "Clear")
            }
        }
    }
}