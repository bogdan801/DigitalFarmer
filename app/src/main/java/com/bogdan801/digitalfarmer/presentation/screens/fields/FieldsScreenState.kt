package com.bogdan801.digitalfarmer.presentation.screens.fields

import com.bogdan801.digitalfarmer.domain.model.Field
import com.bogdan801.digitalfarmer.domain.model.Shape

data class FieldsScreenState(
    val currentPage: CurrentPage = CurrentPage.Fields,
    val listOfFields: List<Field> = listOf()
)

enum class CurrentPage {
    Fields,
    Calculator,
    Settings
}

