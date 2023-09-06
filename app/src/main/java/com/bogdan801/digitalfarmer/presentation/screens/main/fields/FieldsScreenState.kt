package com.bogdan801.digitalfarmer.presentation.screens.main.fields

import androidx.compose.foundation.lazy.LazyListState
import com.bogdan801.digitalfarmer.R
import com.bogdan801.digitalfarmer.domain.model.Field

data class FieldsScreenState(
    val listOfFields: List<Field> = listOf(),
    val cardState: Map<String, Boolean> = mutableMapOf(),
    val loadingCards: Map<String, Boolean> = mutableMapOf(),
    val currentSortMethod: SortMethod = SortMethod.Name,
    val shouldShowSortingOptions: Boolean = false,
    val lazyColumnState: LazyListState = LazyListState(),
)


enum class SortMethod(val localeStringID: Int){
    Name(R.string.sort_name),
    Crop(R.string.sort_crop),
    PlantingDate(R.string.sort_planting_date),
    HarvestDate(R.string.sort_harvest_date)
}