package com.bogdan801.digitalfarmer.presentation.screens.fields

data class FieldsScreenState(
    val currentPage: CurrentPage = CurrentPage.Fields
)

enum class CurrentPage {
    Fields,
    Calculator,
    Settings
}

