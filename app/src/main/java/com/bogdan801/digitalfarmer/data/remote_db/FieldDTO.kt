package com.bogdan801.digitalfarmer.data.remote_db

import com.bogdan801.digitalfarmer.data.util.toLocalDateTime
import com.bogdan801.digitalfarmer.domain.model.Crop
import com.bogdan801.digitalfarmer.domain.model.Field
import com.bogdan801.digitalfarmer.domain.model.Shape

data class FieldDTO(
    val name: String = "",
    val shape: String = "",
    val plantedCrop: String? = null,
    val plantDate: Long? = null,
    val harvestDate: Long? = null,
){
    fun toField() = Field(
        name = name,
        shape = Shape(shape),
        plantedCrop = if(plantedCrop == null) null else Crop.valueOf(plantedCrop),
        plantDate = plantDate?.toLocalDateTime(),
        harvestDate = harvestDate?.toLocalDateTime()
    )
}
