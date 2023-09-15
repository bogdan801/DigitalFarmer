package com.bogdan801.digitalfarmer.domain.model

import com.bogdan801.digitalfarmer.R

enum class SortMethod(val localeStringID: Int){
    Name(R.string.sort_name),
    Area(R.string.sort_area),
    Crop(R.string.sort_crop),
    PlantingDate(R.string.sort_planting_date),
    HarvestDate(R.string.sort_harvest_date)
}