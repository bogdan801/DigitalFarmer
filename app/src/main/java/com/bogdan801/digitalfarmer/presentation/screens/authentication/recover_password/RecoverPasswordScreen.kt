package com.bogdan801.digitalfarmer.presentation.screens.authentication.recover_password

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController

@Composable
fun RecoverPasswordScreen(
    navController: NavHostController,
    viewModel: RecoverPasswordViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.screenState.collectAsStateWithLifecycle()
    if(!state.isWaitingForCode){
        Box(modifier = Modifier.fillMaxSize()){

        }
    }
}