package com.bogdan801.digitalfarmer.presentation.screens.fields

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
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
        val europe = LatLng(49.0, 14.0)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(europe, 3.5f)
        }
        val properties by remember {
            mutableStateOf(MapProperties(mapType = MapType.HYBRID))
        }

        var shape by remember { mutableStateOf(listOf<LatLng>()) }
        val area by remember { derivedStateOf { SphericalUtil.computeArea(shape).toInt() } }
        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            properties = properties,
            cameraPositionState = cameraPositionState,
            onMapLongClick = {
                shape = shape + listOf(it)
            }
        ){
            shape.forEach{
                Marker(state = MarkerState(position = it))

            }
            Polyline(points = shape)
            /*if(area != null) {
                Polygon(
                    points = shape,
                    fillColor = Color.Transparent,
                    strokeColor = MaterialTheme.colorScheme.primary,
                    strokeWidth = 10f
                )
            }*/
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(text = area.toString())

            Button(onClick = { shape = listOf() }) {
                Text(text = "Clear")
            }
        }
    }
}