package com.bogdan801.digitalfarmer.presentation.screens.main.home

data class HomeScreenState(
    val currentPage: CurrentPage = CurrentPage.Fields
)

enum class CurrentPage {
    Fields,
    Calculator,
    Settings
}
