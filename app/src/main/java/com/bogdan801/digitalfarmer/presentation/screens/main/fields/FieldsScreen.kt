package com.bogdan801.digitalfarmer.presentation.screens.main.fields

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.bogdan801.digitalfarmer.presentation.composables.FieldCard
import com.bogdan801.digitalfarmer.presentation.util.containerColor
import com.bogdan801.digitalfarmer.presentation.util.getDeviceConfiguration

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FieldsScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: FieldsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val config = LocalConfiguration.current
    val screenConfig by remember { mutableStateOf(getDeviceConfiguration(config)) }
    val scope = rememberCoroutineScope()
    val state by viewModel.screenState.collectAsStateWithLifecycle()

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    BackHandler(enabled = true) {
        viewModel.unselectAllCards()
        if(state.backExitFlag) (context as Activity).finishAndRemoveTask()
        else Toast.makeText(context, "Tap again to exit", Toast.LENGTH_SHORT).show()
        viewModel.setBackPressTimer()
    }
    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .clickable(
                interactionSource = remember {
                    MutableInteractionSource()
                },
                indication = null,
                onClick = {
                    viewModel.unselectAllCards()
                }
            ),
        topBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                TopAppBar(
                    modifier = Modifier.fillMaxWidth(),
                    title = {
                        Text(text = "Your Fields")
                    },
                    scrollBehavior = scrollBehavior,
                    actions = {
                        IconButton(
                            onClick = {
                                viewModel.flipShowSortingOptions()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Sort,
                                contentDescription = "Sort options"
                            )
                        }
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Options"
                            )
                        }
                    }
                )
                AnimatedVisibility(visible = state.shouldShowSortingOptions) {
                    val colorTransitionFraction = scrollBehavior.state.overlappedFraction
                    val fraction = if (colorTransitionFraction > 0.01f) 1f else 0f
                    val appBarContainerColor by animateColorAsState(
                        targetValue = containerColor(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp), fraction),
                        animationSpec = spring(stiffness = Spring.StiffnessMediumLow), label = ""
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(appBarContainerColor)
                            .padding(horizontal = 16.dp)
                    ) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Sort fields by")
                        FlowRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp, bottom = 8.dp)
                        ){
                            SortMethod.values().forEach { method ->
                                InputChip(
                                    selected = state.currentSortMethod == method,
                                    onClick = { viewModel.selectSortMethod(method) },
                                    label = { Text(stringResource(id = method.localeStringID)) }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                        }
                        if(fraction == 0f) Divider()
                    }
                }
            }

        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
        ) {
            LazyColumn(
                state = state.lazyColumnState,
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                items(
                    items = state.listOfFields,
                    key = {it.id}
                ){field ->
                    FieldCard(
                        modifier = Modifier
                            .widthIn(max = 500.dp),
                        field = field,
                        widthRatio = 5.0,
                        heightRatio = 4.0,
                        isExpanded = state.cardExpansionState[field.id] ?: false,
                        isSelected = state.cardSelectionState[field.id] ?: false,
                        onClick = {
                            viewModel.flipCardExpandState(field.id)
                            viewModel.unselectAllCards()
                        },
                        onLongClick = {
                            viewModel.flipCardSelectionState(field.id)
                        },
                        onMapStartedLoading = {
                            viewModel.setCardLoadingStatus(field.id, true)
                        },
                        onMapFinishedLoading = {
                            viewModel.setCardLoadingStatus(field.id, false)
                            viewModel.updateCardExpansionState(field.id, false)
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

/*            val buttonWidth = 130.dp
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
            }*/

        }

    }

}