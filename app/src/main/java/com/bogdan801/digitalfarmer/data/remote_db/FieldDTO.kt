package com.bogdan801.digitalfarmer.data.remote_db

import com.bogdan801.digitalfarmer.data.util.toEpoch
import com.bogdan801.digitalfarmer.data.util.toLocalDateTime
import com.bogdan801.digitalfarmer.domain.model.Crop
import com.bogdan801.digitalfarmer.domain.model.Field
import com.bogdan801.digitalfarmer.domain.model.Shape

data class FieldDTO(
    val id: String = "",
    val name: String = "",
    val shape: String = "",
    val plantedCrop: String? = null,
    val plantDate: Long? = null,
    val harvestDate: Long? = null,
){
    fun toField() = Field(
        id = id,
        name = name,
        shape = Shape(shape),
        plantedCrop = if(plantedCrop == null) null else Crop.valueOf(plantedCrop),
        plantDate = plantDate?.toLocalDateTime(),
        harvestDate = harvestDate?.toLocalDateTime()
    )
}

fun Field.toFieldDTO(): FieldDTO = FieldDTO(
    id = id,
    name = name,
    shape = shape.toString(),
    plantedCrop = plantedCrop?.name,
    plantDate = plantDate?.toEpoch(),
    harvestDate = harvestDate?.toEpoch()
)
