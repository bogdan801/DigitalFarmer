package com.bogdan801.digitalfarmer.presentation.login

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)
