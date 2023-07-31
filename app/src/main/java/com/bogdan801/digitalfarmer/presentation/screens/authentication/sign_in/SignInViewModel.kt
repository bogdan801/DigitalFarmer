package com.bogdan801.digitalfarmer.presentation.screens.authentication.sign_in

import androidx.lifecycle.ViewModel
import com.bogdan801.digitalfarmer.data.login.AuthUIClient
import com.bogdan801.digitalfarmer.data.login.SignInResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SignInViewModel
@Inject
constructor(
    val authUIClient: AuthUIClient
): ViewModel() {
    private val _state = MutableStateFlow(SignInScreenState())
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
            SignInScreenState()
        }
    }

    fun updateEmail(newString: String){
        _state.update {
            it.copy(
                email = newString
            )
        }
    }

    fun updatePassword(newString: String){
        _state.update {
            it.copy(
                password = newString
            )
        }
    }
}