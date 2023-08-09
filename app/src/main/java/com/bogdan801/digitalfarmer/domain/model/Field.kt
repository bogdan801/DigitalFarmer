package com.bogdan801.digitalfarmer.domain.model

import kotlinx.datetime.LocalDateTime

data class Field(
    val name: String,
    val shape: Shape = Shape(),
    val plantedCrop: Crop? = null,
    val plantDate: LocalDateTime? = null,
    val harvestDate: LocalDateTime? = null,
)
