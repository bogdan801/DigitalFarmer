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
import com.bogdan801.digitalfarmer.domain.model.Crop
import com.bogdan801.digitalfarmer.domain.model.Field
import com.bogdan801.digitalfarmer.domain.model.Shape
import com.bogdan801.digitalfarmer.presentation.navigation.Screen
import com.google.maps.android.compose.GoogleMap
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import java.time.Month

@Composable
fun FieldsScreen(
    navController: NavHostController,
    viewModel: FieldsViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    //val context = LocalContext.current
    //val userData = remember { viewModel.authUIClient.getSignedInUser() }
    //val state by viewModel.screenState.collectAsStateWithLifecycle()

    /*ShapeEditorMap(
        modifier = Modifier.fillMaxSize(),
        shape = state.shape,
        onShapeChanged = { newShape ->
            viewModel.updateShape(newShape)
        }
    )*/

    GoogleMap(

    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                viewModel.addField(
                    Field(
                        name = "За равцом",
                        shape = Shape("51.798806,33.053786;51.798843,33.053908;51.798268,33.054454;51.798268,33.054454;51.798806,33.053786"),
                        plantedCrop = Crop.Potato,
                        plantDate = LocalDateTime(year = 2023, month = Month.APRIL, dayOfMonth = 26, hour = 10, minute = 0),
                        harvestDate = LocalDateTime(year = 2023, month = Month.SEPTEMBER, dayOfMonth = 2, hour = 14, minute = 40)
                    )
                )
            }
        ) {
            Text("Add field")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                viewModel.updateField(
                    Field(
                        name = "За рівцем",
                        shape = Shape("52.798806,33.053786;51.798843,33.053908;51.798268,33.054454;51.798268,33.054454;52.798806,33.053786"),
                        plantedCrop = Crop.Potato,
                        plantDate = LocalDateTime(year = 2023, month = Month.APRIL, dayOfMonth = 27, hour = 10, minute = 0),
                        harvestDate = LocalDateTime(year = 2023, month = Month.SEPTEMBER, dayOfMonth = 5, hour = 14, minute = 40)
                    ),
                    1
                )
            }
        ) {
            Text("Update field")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                viewModel.deleteField(1)
            }
        ) {
            Text("Delete field")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                scope.launch {
                    viewModel.authUIClient.signOut()
                    navController.navigate(Screen.SignInScreen.route){
                        popUpTo(0)
                    }
                }
            }
        ) {
            Text("Log out")
        }
    }
}