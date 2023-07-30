package com.bogdan801.digitalfarmer.presentation.screens.authentication.sign_in

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.bogdan801.digitalfarmer.data.login.ErrorType
import com.bogdan801.digitalfarmer.presentation.navigation.Screen
import kotlinx.coroutines.launch

@Composable
fun SignInScreen(
    navController: NavHostController,
    viewModel: SignInViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val state by viewModel.state.collectAsStateWithLifecycle()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if(result.resultCode == Activity.RESULT_OK){
                scope.launch {
                    val signInResult = viewModel.authUIClient.signInWithIntent(result.data ?: return@launch)
                    viewModel.onSignInResult(signInResult = signInResult)
                }
            }
        }
    )

    LaunchedEffect(key1 = state.isSignInSuccessful){
        if (state.isSignInSuccessful){
            navController.navigate(Screen.FieldsScreen.route){
                popUpTo(0)
            }
            viewModel.resetState()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = state.email,
                onValueChange = { viewModel.updateEmail(it) }
            )
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = state.password,
                onValueChange = { viewModel.updatePassword(it) },
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                modifier = Modifier.width(200.dp),
                onClick = {
                    if(state.email.isBlank() || state.password.isBlank()) {
                        Toast.makeText(context, "Email and password fields can't be empty", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if(state.password.length < 8){
                        Toast.makeText(context, "Password must me at least 8 characters long", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    scope.launch {
                        val signInResult = viewModel.authUIClient.signInWithEmailAndPassword(state.email, state.password)
                        if(signInResult.errorType == null){
                            viewModel.onSignInResult(signInResult = signInResult)
                        }
                        else {
                            when (signInResult.errorType) {
                                ErrorType.WrongEmailFormat -> {
                                    Toast.makeText(context, "Wrong email format", Toast.LENGTH_SHORT).show()
                                }
                                ErrorType.WeakPassword -> {
                                    Toast.makeText(context, "Weak password", Toast.LENGTH_SHORT).show()
                                }
                                ErrorType.WrongEmailOrPassWord -> {
                                    Toast.makeText(context, "Wrong email or password", Toast.LENGTH_SHORT).show()
                                     viewModel.updateShowForgotPassword(true)
                                }
                                ErrorType.NoInternetConnection -> {
                                    Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show()
                                }
                                ErrorType.Other -> {
                                    Toast.makeText(context, signInResult.errorMessage, Toast.LENGTH_SHORT).show()
                                }
                                else -> {}
                            }
                        }
                    }
                }
            ) {
                Text(text = "Log in")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                modifier = Modifier.width(200.dp),
                onClick = {
                    scope.launch {
                        val signInIntentSender = viewModel.authUIClient.getSignInWithGoogleIntentSender()
                        launcher.launch(
                            IntentSenderRequest.Builder(signInIntentSender ?: return@launch).build()
                        )
                    }
                }
            ) {
                Text(text = "Sign in with Google")
            }
            AnimatedVisibility(visible = state.showForgotPassword) {
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(
                    onClick = {
                        navController.navigate(Screen.RecoverPasswordScreen.route)
                    }
                ) {
                    Text(text = "Forgot the password? Recover")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(
                onClick = {
                    navController.navigate(Screen.RegisterScreen.route)
                }
            ) {
                Text(text = "Don't have an account yet? Sign up")
            }
        }
    }
}