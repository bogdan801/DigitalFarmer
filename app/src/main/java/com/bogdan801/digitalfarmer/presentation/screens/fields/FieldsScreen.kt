package com.bogdan801.digitalfarmer.presentation.screens.fields

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

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
        val singapore = LatLng(1.35, 103.87)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(singapore, 10f)
        }
        val properties by remember {
            mutableStateOf(MapProperties(mapType = MapType.HYBRID))
        }
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            properties = properties,
            cameraPositionState = cameraPositionState,
        ) {
            Marker(
                state = MarkerState(position = singapore),
                title = "Singapore",
                snippet = "Marker in Singapore"
            )
        }
    }


    /*Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        userData?.profilePictureUrl?.let { url ->
            AsyncImage(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape),
                model = url,
                contentDescription = "Profile picture",
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = userData?.username.toString())
        Text(text = userData?.userID.toString())
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                scope.launch {
                    viewModel.authUIClient.signOut()
                    Toast.makeText(context, "Signed out", Toast.LENGTH_SHORT).show()
                    navController.navigate(Screen.SignInScreen.route){
                        popUpTo(0)
                    }
                }
            }
        ) {
            Text(text = "Sign out")
        }
    }*/

}