package com.bogdan801.digitalfarmer.presentation.screens.fields

import com.bogdan801.digitalfarmer.domain.model.Shape

data class FieldsScreenState(
    val currentPage: CurrentPage = CurrentPage.Fields,
    val shape: Shape = Shape()
)

enum class CurrentPage {
    Fields,
    Calculator,
    Settings
}

