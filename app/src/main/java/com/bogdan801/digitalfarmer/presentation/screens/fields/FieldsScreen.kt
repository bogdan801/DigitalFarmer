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
import com.bogdan801.digitalfarmer.presentation.composables.ShapeEditorMap
import com.bogdan801.digitalfarmer.presentation.navigation.Screen
import kotlinx.coroutines.launch

@Composable
fun FieldsScreen(
    navController: NavHostController,
    viewModel: FieldsViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val userData = remember { viewModel.authUIClient.getSignedInUser() }
    val state by viewModel.screenState.collectAsStateWithLifecycle()

    ShapeEditorMap(
        modifier = Modifier.fillMaxSize(),
        shape = state.shape,
        onShapeChanged = { newShape ->
            viewModel.updateShape(newShape)
        }
    )

    /*Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

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
    }*/
}