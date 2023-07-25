package com.bogdan801.digitalfarmer.presentation.screens.sign_in

import androidx.lifecycle.ViewModel
import com.bogdan801.digitalfarmer.presentation.login.SignInResult
import com.bogdan801.digitalfarmer.presentation.login.SignInState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SignInViewModel: ViewModel() {
    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    fun onSignInResult(signInResult: SignInResult){
        _state.update {
            it.copy(
                isSignInSuccessful = signInResult.userData != null,
                signInError = signInResult.errorMessage
            )
        }
    }

    fun resetState(){
        _state.update {
            SignInState()
        }
    }
}