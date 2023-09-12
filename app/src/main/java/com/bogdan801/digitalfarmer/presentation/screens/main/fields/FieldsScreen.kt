package com.bogdan801.digitalfarmer.presentation.screens.main.fields

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.bogdan801.digitalfarmer.domain.model.Crop
import com.bogdan801.digitalfarmer.domain.model.Field
import com.bogdan801.digitalfarmer.domain.model.Shape
import com.bogdan801.digitalfarmer.presentation.composables.AlertDialogBox
import com.bogdan801.digitalfarmer.presentation.composables.FieldCard
import com.bogdan801.digitalfarmer.presentation.util.containerColor
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class,
    ExperimentalAnimationApi::class
)
@Composable
fun FieldsScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: FieldsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.screenState.collectAsStateWithLifecycle()

    // Visibility for FAB
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                // Hide FAB
                if (available.y < -1 && state.lazyColumnState.canScrollForward) {
                    viewModel.setFABState(false)
                }

                // Show FAB
                if (available.y > 1) {
                    viewModel.setFABState(true)
                }

                return Offset.Zero
            }
        }
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
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
                //top app bar
                TopAppBar(
                    modifier = Modifier.fillMaxWidth(),
                    title = {
                        Text(text = "Your Fields")
                    },
                    scrollBehavior = scrollBehavior,
                    actions = {
                        AnimatedContent(
                            targetState = viewModel.isAnyCardSelected,
                            label = ""
                        ) {isAnyCardSelected ->
                            if(isAnyCardSelected){
                                IconButton(
                                    onClick = {
                                        viewModel.deleteSelectedFields()
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete selected fields"
                                    )
                                }
                            }
                            else {
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
                            }
                        }

                        //dropdown menu states
                        var showDropDownMenu by remember {
                            mutableStateOf(false)
                        }
                        IconButton(
                            onClick = {
                                showDropDownMenu = true
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Options"
                            )
                            DropdownMenu(
                                expanded = showDropDownMenu,
                                onDismissRequest = { showDropDownMenu = false }
                            ) {
                                DropdownMenuItem(
                                    text = {
                                        Text("Delete all fields")
                                    },
                                    onClick = {
                                        showDropDownMenu = false
                                        viewModel.setDeleteAllDialogBoxState(true)
                                    }
                                )
                            }
                        }
                    }
                )
                //sorting bar
                AnimatedVisibility(visible = state.showSortingOptions) {
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

        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = state.showFAB,
                enter = slideInVertically(initialOffsetY = { it * 2 }),
                exit = slideOutVertically(targetOffsetY = { it * 2 }),
            ) {
                FloatingActionButton(
                    onClick = {
                        viewModel.addField(
                            Field(
                                name = "Город",
                                shape = Shape("51.799805,33.053209;51.799795,33.053360;51.799136,33.053382;51.799119, 33.053221;51.799805,33.053209"),
                                plantedCrop = Crop.values()[Random.nextInt(Crop.values().size)],
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
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "")
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Add field")
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
                    .fillMaxSize()
                    .nestedScroll(nestedScrollConnection),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 8.dp,
                    bottom = 72.dp
                ),
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
                            viewModel.flipShowSortingOptions(false)
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
            AlertDialogBox(
                isShown = state.showDeleteDialog,
                onDismissRequest = { viewModel.setDeleteAllDialogBoxState(false) },
                onConfirmation = {
                    viewModel.setDeleteAllDialogBoxState(false)
                    viewModel.deleteAllFields()
                },
                dialogTitle = "Delete all fields?",
                dialogText = "Are you sure you want to delete all fields? Deleted fields can't be restored"
            )
        }
    }
    BackHandler(enabled = true) {
        viewModel.unselectAllCards()
        if(state.backExitFlag) (context as Activity).finishAndRemoveTask()
        else Toast.makeText(context, "Tap again to exit", Toast.LENGTH_SHORT).show()
        viewModel.setBackPressTimer()
    }
}