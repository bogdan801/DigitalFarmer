package com.bogdan801.digitalfarmer.presentation.screens.register

import androidx.lifecycle.ViewModel
import com.bogdan801.digitalfarmer.data.login.AuthUIClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class RegisterScreenViewModel
@Inject constructor(
    val authUIClient: AuthUIClient
) : ViewModel() {
    private val _screenState = MutableStateFlow(RegisterScreenState())
    val screenState = _screenState.asStateFlow()

    fun updateUsername(newString: String){
        _screenState.update {
            it.copy(userName = newString)
        }
    }

    fun updateEmail(newString: String){
        _screenState.update {
            it.copy(email = newString)
        }
    }

    fun updatePassword(newString: String){
        _screenState.update {
            it.copy(password = newString)
        }
    }

    fun updatePasswordConfirm(newString: String){
        _screenState.update {
            it.copy(passwordConfirm = newString)
        }
    }
}