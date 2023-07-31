package com.bogdan801.digitalfarmer.presentation.screens.authentication.recover_password

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController

@Composable
fun RecoverPasswordScreen(
    navController: NavHostController,
    viewModel: RecoverPasswordViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val focusManager  = LocalFocusManager.current
    val state by viewModel.screenState.collectAsStateWithLifecycle()
    Box(modifier = Modifier
        .fillMaxSize()
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = {
                focusManager.clearFocus()
            }
        )
        .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ){
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if(!state.hasEmailBeenSent){
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.address,
                    onValueChange = { viewModel.updateEmailString(it) },
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
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        viewModel.authUIClient.sendRecoverPasswordEmail(
                            address = state.address,
                            onSuccess = {
                                Toast.makeText(context, "Email sent", Toast.LENGTH_LONG).show()
                                viewModel.setEmailBeenSetFlag(true)
                            },
                            onError = {
                                Toast.makeText(context, it::class.simpleName + ": " + it.message, Toast.LENGTH_LONG).show()
                            }
                        )
                    }
                ) {
                    Text(text = "Send recovery email")
                }
            }
            else {
                Text(text = "Password recovery link has been sent to ${state.address}")
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {navController.popBackStack()}
                ) {
                    Text(text = "Go back to login")
                }
            }
        }
    }
}