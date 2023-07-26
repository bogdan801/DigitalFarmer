package com.bogdan801.digitalfarmer.presentation.screens.fields

import androidx.lifecycle.ViewModel
import com.bogdan801.digitalfarmer.presentation.screens.sign_in.SignInState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class FieldsViewModel @Inject constructor() : ViewModel() {
    private val _screenState = MutableStateFlow(SignInState())
    val screenState = _screenState.asStateFlow()
}