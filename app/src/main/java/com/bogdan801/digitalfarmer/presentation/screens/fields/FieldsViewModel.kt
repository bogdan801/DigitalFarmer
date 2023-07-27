package com.bogdan801.digitalfarmer.presentation.screens.fields

import androidx.lifecycle.ViewModel
import com.bogdan801.digitalfarmer.data.login.AuthUIClient
import com.bogdan801.digitalfarmer.presentation.screens.sign_in.SignInScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class FieldsViewModel
@Inject
constructor(
    val authUIClient: AuthUIClient
): ViewModel() {
    private val _screenState = MutableStateFlow(FieldsScreenState())
    val screenState = _screenState.asStateFlow()
}