package com.bogdan801.digitalfarmer.domain.repository

import com.bogdan801.digitalfarmer.data.remote_db.ActionResult
import com.bogdan801.digitalfarmer.domain.model.Field

interface Repository {
    fun addField(field: Field): ActionResult<Field>

    suspend fun editField(newField: Field): ActionResult<Field>

    suspend fun deleteField(index: Int): ActionResult<Field>
    fun deleteField(field: Field): ActionResult<Field>

    fun addFieldsListener(listener: (ActionResult<List<Field>>) -> Unit)
}