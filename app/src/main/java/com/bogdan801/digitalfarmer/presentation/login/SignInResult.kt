package com.bogdan801.digitalfarmer.presentation.login

data class SignInResult(
    val userData: UserData?,
    val errorMessage: String? = null
)

data class UserData(
    val userID: String,
    val username: String?,
    val profilePictureUrl: String?
)