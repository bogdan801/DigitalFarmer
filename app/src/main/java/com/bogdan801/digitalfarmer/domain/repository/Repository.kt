package com.bogdan801.digitalfarmer.domain.repository

import com.bogdan801.digitalfarmer.data.remote_db.ActionResult
import com.bogdan801.digitalfarmer.domain.model.Field

interface Repository {
    fun addField(field: Field): ActionResult<Field>

    fun editField(field: Field, id: Int): ActionResult<Field>

    fun deleteField(field: Field, id: Int): ActionResult<Field>

    fun addFieldsListener(listener: (ActionResult<List<Field>>) -> Unit)
}