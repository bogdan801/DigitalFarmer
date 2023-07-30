package com.bogdan801.digitalfarmer.presentation.navigation

sealed class Screen(val route: String){
    object SplashScreen: Screen("splash")
    object SignInScreen: Screen("signin")
    object RegisterScreen: Screen("register")
    object FieldsScreen: Screen("fields")
}
