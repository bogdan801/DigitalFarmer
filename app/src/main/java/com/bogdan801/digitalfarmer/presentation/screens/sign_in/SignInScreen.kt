package com.bogdan801.digitalfarmer.presentation.screens.sign_in

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.bogdan801.digitalfarmer.presentation.login.GoogleAuthUIClient
import com.bogdan801.digitalfarmer.presentation.navigation.Screen
import kotlinx.coroutines.launch

@Composable
fun SignInScreen(
    navController: NavHostController,
    viewModel: SignInViewModel = hiltViewModel(),
    googleAuthUIClient: GoogleAuthUIClient
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = state.signInError){
        state.signInError?.let {
            Toast.makeText(context, "Log In error: ${state.signInError}", Toast.LENGTH_SHORT).show()
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if(result.resultCode == Activity.RESULT_OK){
                scope.launch {
                    val signInResult = googleAuthUIClient.signInWithIntent(result.data ?: return@launch)
                    viewModel.onSignInResult(signInResult = signInResult)
                }
            }
        }
    )

    LaunchedEffect(key1 = state.isSignInSuccessful){
        if(state.isSignInSuccessful){
            Toast.makeText(context, "Sign in successful", Toast.LENGTH_SHORT).show()
            navController.navigate(Screen.FieldsScreen.route)
            viewModel.resetState()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Button(
            onClick = {
                scope.launch {
                    val signInIntentSender = googleAuthUIClient.signIn()
                    launcher.launch(
                        IntentSenderRequest.Builder(signInIntentSender ?: return@launch).build()
                    )
                }
            }
        ) {
            Text(text = "Sign in with Google")
        }
    }
}