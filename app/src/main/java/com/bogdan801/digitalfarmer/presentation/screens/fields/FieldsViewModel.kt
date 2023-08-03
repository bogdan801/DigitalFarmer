package com.bogdan801.digitalfarmer.presentation.screens.fields

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.bogdan801.digitalfarmer.data.login.AuthUIClient
import com.google.android.gms.maps.model.LatLng
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
    var shape by mutableStateOf(listOf<LatLng>())

    private val _screenState = MutableStateFlow(FieldsScreenState())
    val screenState = _screenState.asStateFlow()
}