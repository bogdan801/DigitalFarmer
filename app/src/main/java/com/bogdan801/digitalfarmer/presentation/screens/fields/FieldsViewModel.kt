package com.bogdan801.digitalfarmer.presentation.screens.fields

import androidx.lifecycle.ViewModel
import com.bogdan801.digitalfarmer.data.login.AuthUIClient
import com.bogdan801.digitalfarmer.domain.model.Shape
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class FieldsViewModel
@Inject
constructor(
    val authUIClient: AuthUIClient
): ViewModel() {
    private val _screenState = MutableStateFlow(FieldsScreenState())
    val screenState = _screenState.asStateFlow()

    fun updateShape(newShape: Shape){
        _screenState.update {
            it.copy(shape = newShape)
        }
    }
}