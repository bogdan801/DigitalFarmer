package com.bogdan801.digitalfarmer.presentation.screens.main.fields

import androidx.compose.foundation.lazy.LazyListState
import com.bogdan801.digitalfarmer.domain.model.Field
import com.bogdan801.digitalfarmer.domain.model.SortMethod

data class FieldsScreenState(
    val listOfFields: List<Field> = listOf(),
    val cardExpansionState: Map<String, Boolean> = mutableMapOf(),
    val cardSelectionState: Map<String, Boolean> = mutableMapOf(),
    val loadingCards: Map<String, Boolean> = mutableMapOf(),
    val currentSortMethod: SortMethod = SortMethod.Name,
    val lazyColumnState: LazyListState = LazyListState(),
    val backExitFlag: Boolean = false,
    val showSortingOptions: Boolean = false,
    val showFAB: Boolean = true,
    val showDeleteDialog: Boolean = false
)