package com.bogdan801.digitalfarmer.presentation.screens.main.home

import androidx.lifecycle.ViewModel
import com.bogdan801.digitalfarmer.domain.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject
constructor(
    private val repository: Repository
): ViewModel() {
    private val _screenState = MutableStateFlow(HomeScreenState())
    val screenState = _screenState.asStateFlow()

    /*fun setCurrentPage(page: CurrentPage){
        _screenState.update {
            _screenState.value.copy(currentPage = page)
        }
    }*/
}