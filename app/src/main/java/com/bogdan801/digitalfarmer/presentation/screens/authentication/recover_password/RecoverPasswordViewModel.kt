package com.bogdan801.digitalfarmer.presentation.screens.authentication.recover_password

import androidx.lifecycle.ViewModel
import com.bogdan801.digitalfarmer.data.login.AuthUIClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class RecoverPasswordViewModel
@Inject
constructor(
    val authUIClient: AuthUIClient
) : ViewModel() {
    private val _screenState = MutableStateFlow(RecoverPasswordScreenState())
    val screenState = _screenState.asStateFlow()

    fun updateEmailString(newString: String){
        _screenState.update {
            it.copy(address = newString)
        }
    }

    fun setEmailBeenSetFlag(value: Boolean){
        _screenState.update {
            it.copy(hasEmailBeenSent = value)
        }
    }
}