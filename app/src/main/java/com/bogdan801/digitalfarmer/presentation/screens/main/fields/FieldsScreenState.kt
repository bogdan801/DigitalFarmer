package com.bogdan801.digitalfarmer.presentation.screens.main.fields

import com.bogdan801.digitalfarmer.domain.model.Field

data class FieldsScreenState(
    val listOfFields: List<Field> = listOf(),
    val cardState: Map<String, Boolean> = mutableMapOf(),
    val loadingCards: Map<String, Boolean> = mutableMapOf()
)


