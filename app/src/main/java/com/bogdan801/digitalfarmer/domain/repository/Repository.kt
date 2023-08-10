package com.bogdan801.digitalfarmer.domain.repository

import com.bogdan801.digitalfarmer.data.remote_db.ActionResult
import com.bogdan801.digitalfarmer.domain.model.Field

interface Repository {
    fun addField(field: Field): ActionResult<Field>

    suspend fun editField(newField: Field, id: Int): ActionResult<Field>

    suspend fun deleteField(id: Int): ActionResult<Field>

    fun addFieldsListener(listener: (ActionResult<List<Field>>) -> Unit)
}