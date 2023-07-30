package com.bogdan801.digitalfarmer.presentation.screens.authentication.recover_password

data class RecoverPasswordScreenState(
    val email: String = "",
    val isWaitingForCode: Boolean = false,
    val codeText: String = ""
)
