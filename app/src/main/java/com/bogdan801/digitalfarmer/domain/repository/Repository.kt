package com.bogdan801.digitalfarmer.domain.repository

import com.bogdan801.digitalfarmer.domain.util.ActionResult
import com.bogdan801.digitalfarmer.domain.model.Field
import com.bogdan801.digitalfarmer.domain.model.SortMethod

interface Repository {
    //database
    fun addField(field: Field): ActionResult<Field>

    suspend fun editField(newField: Field): ActionResult<Field>

    fun deleteField(id: String): ActionResult<Field>

    fun addFieldsListener(listener: (ActionResult<List<Field>>) -> Unit)

    //settings
    suspend fun setSortingMethodSetting(sortMethod: SortMethod)
    suspend fun getSortingMethodSetting(): SortMethod?
}