package com.bogdan801.digitalfarmer.presentation.screens.authentication.register

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.bogdan801.digitalfarmer.data.login.ErrorType
import com.bogdan801.digitalfarmer.presentation.navigation.Screen
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    navController: NavHostController,
    viewModel: RegisterScreenViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val focusManager  = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    val state by viewModel.screenState.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    focusManager.clearFocus()
                }
            ),
        contentAlignment = Alignment.Center
    ){
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = state.userName,
                onValueChange = { viewModel.updateUsername(it) },
                placeholder = {
                    Text("Name")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = state.email,
                onValueChange = { viewModel.updateEmail(it) },
                placeholder = {
                    Text("Email")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = state.password,
                onValueChange = { viewModel.updatePassword(it) },
                visualTransformation = PasswordVisualTransformation(),
                placeholder = {
                    Text("Password")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = state.passwordConfirm,
                onValueChange = { viewModel.updatePasswordConfirm(it) },
                visualTransformation = PasswordVisualTransformation(),
                placeholder = {
                    Text("Repeat password")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                modifier = Modifier.width(200.dp),
                onClick = {
                    if(state.email.isBlank()) {
                        Toast.makeText(context, "Email field can't be empty", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if(state.password.length < 8){
                        Toast.makeText(context, "Password must me at least 8 characters long", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if(state.password != state.passwordConfirm){
                        Toast.makeText(context, "Passwords don't match", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    scope.launch {
                        val result = viewModel.authUIClient.createUserAndSignIn(state.userName, state.email, state.password)
                        when(result.errorType){
                            ErrorType.AccountAlreadyExists -> {
                                Toast.makeText(context, "Account already exists", Toast.LENGTH_SHORT).show()
                            }
                            ErrorType.WrongEmailFormat -> {
                                Toast.makeText(context, "Wrong email format", Toast.LENGTH_SHORT).show()
                            }
                            ErrorType.WeakPassword -> {
                                Toast.makeText(context, "Weak password", Toast.LENGTH_SHORT).show()
                            }
                            ErrorType.WrongEmailOrPassWord -> {}
                            ErrorType.NoInternetConnection -> {
                                Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show()
                            }
                            ErrorType.Other -> {
                                Toast.makeText(context, result.errorMessage, Toast.LENGTH_SHORT).show()
                            }
                            null -> {
                                navController.navigate(Screen.HomeScreen.route){
                                    popUpTo(0)
                                }
                            }
                        }
                    }
                }
            ) {
                Text(text = "Register")
            }
        }
    }
}