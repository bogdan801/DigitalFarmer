package com.bogdan801.digitalfarmer.presentation.navigation

sealed class Screen(val route: String){
    object LogInScreen: Screen("login")
    object FieldsScreen: Screen("fields")
}
