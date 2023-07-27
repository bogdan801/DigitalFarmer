package com.bogdan801.digitalfarmer.data.login

import com.bogdan801.digitalfarmer.domain.model.User

data class SignInResult(
    val userData: User?,
    val errorMessage: String? = null,
    val errorType: ErrorType? = null
)

enum class ErrorType {
    AccountAlreadyExists,
    WrongEmailFormat,
    WeakPassword,
    WrongEmailOrPassWord,
    Other
}

