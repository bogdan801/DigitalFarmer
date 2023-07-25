package com.bogdan801.digitalfarmer.presentation.navigation

import android.app.Activity.RESULT_OK
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
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
    NavHost(navController = navController, startDestination = Screen.LogInScreen.route){
        composable(Screen.LogInScreen.route){
            val viewModel = viewModel<SignInViewModel>()
            val signInState by viewModel.state.collectAsStateWithLifecycle()
            val scope = rememberCoroutineScope()

            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult(),
                onResult = { result ->
                    if(result.resultCode == RESULT_OK){
                        scope.launch {
                            val signInResult = googleAuthUIClient.signInWithIntent(result.data ?: return@launch)
                            viewModel.onSignInResult(signInResult = signInResult)
                        }
                    }
                }
            )

            SignInScreen(
                state = signInState,
                onSignInClick = {
                    scope.launch {
                        val signInIntentSender = googleAuthUIClient.signIn()
                        launcher.launch(
                            IntentSenderRequest.Builder(signInIntentSender ?: return@launch).build()
                        )
                    }
                }
            )

        }

        composable(Screen.FieldsScreen.route){

            FieldsScreen()
        }
    }
}