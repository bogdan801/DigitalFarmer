package com.bogdan801.digitalfarmer.presentation.screens.fields

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.bogdan801.digitalfarmer.presentation.composables.ShapeEditorMap

@Composable
fun FieldsScreen(
    navController: NavHostController,
    viewModel: FieldsViewModel = hiltViewModel()
) {
    //val scope = rememberCoroutineScope()
    //val context = LocalContext.current
    //val userData = remember { viewModel.authUIClient.getSignedInUser() }
    val state by viewModel.screenState.collectAsStateWithLifecycle()

    ShapeEditorMap(
        modifier = Modifier.fillMaxSize(),
        shape = state.shape,
        onShapeChanged = { newShape ->
            viewModel.updateShape(newShape)
        }
    )
}