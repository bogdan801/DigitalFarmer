package com.bogdan801.digitalfarmer.presentation.screens.fields

import com.bogdan801.digitalfarmer.domain.model.Field

data class FieldsScreenState(
    val currentPage: CurrentPage = CurrentPage.Fields,
    val listOfFields: List<Field> = listOf(),
    val cardState: Map<String, Boolean> = mutableMapOf()
)

enum class CurrentPage {
    Fields,
    Calculator,
    Settings
}

