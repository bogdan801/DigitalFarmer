package com.bogdan801.digitalfarmer.data.remote_db

import com.bogdan801.digitalfarmer.domain.model.Field


sealed class ActionResult<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T?): ActionResult<T>(data)
    class Error<T>(message: String): ActionResult<T>(message = message)
}