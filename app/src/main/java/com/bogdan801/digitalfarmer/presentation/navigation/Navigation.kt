package com.bogdan801.digitalfarmer.presentation.navigation

import android.app.Activity.RESULT_OK
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bogdan801.digitalfarmer.presentation.login.GoogleAuthUIClient
import com.bogdan801.digitalfarmer.presentation.screens.fields.FieldsScreen
import com.bogdan801.digitalfarmer.presentation.screens.sign_in.SignInViewModel
import com.bogdan801.digitalfarmer.presentation.screens.sign_in.SignInScreen
import kotlinx.coroutines.launch

@Composable
fun Navigation(
    navController: NavHostController,
    googleAuthUIClient: GoogleAuthUIClient
) {
    NavHost(
        navController = navController,
        startDestination = if(googleAuthUIClient.getSignedInUser() != null) Screen.FieldsScreen.route
                           else Screen.LogInScreen.route
    ){
        composable(Screen.LogInScreen.route){
            SignInScreen(
                navController = navController,
                googleAuthUIClient = googleAuthUIClient
            )
        }

        composable(Screen.FieldsScreen.route){
            FieldsScreen(
                navController = navController,
                googleAuthUIClient = googleAuthUIClient
            )
        }
    }
}