package com.bogdan801.digitalfarmer.presentation.screens.fields

data class FieldsScreenState(
    val currentPage: CurrentPage
)

enum class CurrentPage {
    Fields,
    Calculator,
    Settings
}

