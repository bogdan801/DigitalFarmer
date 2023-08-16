package com.bogdan801.digitalfarmer.presentation.screens.fields

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.bogdan801.digitalfarmer.domain.model.Crop
import com.bogdan801.digitalfarmer.domain.model.Field
import com.bogdan801.digitalfarmer.domain.model.Shape
import com.bogdan801.digitalfarmer.presentation.composables.FieldCard
import com.bogdan801.digitalfarmer.presentation.navigation.Screen
import com.bogdan801.digitalfarmer.presentation.util.getDeviceConfiguration
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import java.time.Month

@Composable
fun FieldsScreen(
    navController: NavHostController,
    viewModel: FieldsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val config = LocalConfiguration.current
    val screenConfig by remember { mutableStateOf(getDeviceConfiguration(config)) }
    val scope = rememberCoroutineScope()
    val state by viewModel.screenState.collectAsStateWithLifecycle()


    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            items(
                items = state.listOfFields,
                key = {it.id}
            ){field ->
                FieldCard(
                    modifier = Modifier
                        .widthIn(max = 400.dp),
                    field = field,
                    widthRatio = 5.0,
                    heightRatio = 4.0,
                    isExpanded = state.cardState[field.id] ?: false,
                    onExpandClick = {
                        viewModel.flipCardState(field.id)
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        val buttonWidth = 130.dp
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .alpha(0.3f)
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(
                    modifier = Modifier.width(buttonWidth),
                    onClick = {
                        viewModel.addField(
                            Field(
                                name = "Город",
                                shape = Shape("51.799805,33.053209;51.799795,33.053360;51.799136,33.053382;51.799119, 33.053221;51.799805,33.053209"),
                                plantedCrop = Crop.Potato,
                                plantDate = LocalDateTime(
                                    year = 2023,
                                    month = Month.APRIL,
                                    dayOfMonth = 26,
                                    hour = 10,
                                    minute = 0
                                ),
                                harvestDate = LocalDateTime(
                                    year = 2023,
                                    month = Month.SEPTEMBER,
                                    dayOfMonth = 2,
                                    hour = 14,
                                    minute = 40
                                )
                            )
                        )
                    }
                ) {
                    Text("Add field")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    modifier = Modifier.width(buttonWidth),
                    onClick = {
                        viewModel.updateField(
                            state.listOfFields[1].copy(
                                name = "За рівцем",
                                shape = Shape("52.798806,33.053786;51.798843,33.053908;51.798268,33.054454;51.798268,33.054454;52.798806,33.053786"),
                            )
                        )
                    }
                ) {
                    Text("Update field")
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(
                    modifier = Modifier.width(buttonWidth),
                    onClick = {
                        viewModel.deleteField(state.listOfFields[0])
                    }
                ) {
                    Text("Delete field")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    modifier = Modifier.width(buttonWidth),
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
    }
}