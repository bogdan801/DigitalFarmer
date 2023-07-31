package com.bogdan801.digitalfarmer.presentation.screens.authentication.sign_in

data class SignInScreenState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null,
    val email: String = "",
    val password: String = ""
)
