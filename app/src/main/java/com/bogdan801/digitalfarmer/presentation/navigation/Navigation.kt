package com.bogdan801.digitalfarmer.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bogdan801.digitalfarmer.data.login.AuthUIClient
import com.bogdan801.digitalfarmer.presentation.screens.fields.FieldsScreen
import com.bogdan801.digitalfarmer.presentation.screens.register.RegisterScreen
import com.bogdan801.digitalfarmer.presentation.screens.sign_in.SignInScreen

@Composable
fun Navigation(
    navController: NavHostController,
    authUIClient: AuthUIClient
) {
    NavHost(
        navController = navController,
        startDestination = if(authUIClient.getSignedInUser() != null) Screen.FieldsScreen.route
                           else Screen.SignInScreen.route
    ){
        composable(Screen.SignInScreen.route){
            SignInScreen(
                navController = navController
            )
        }

        composable(Screen.FieldsScreen.route){
            FieldsScreen(
                navController = navController
            )
        }

        composable(Screen.RegisterScreen.route){
            RegisterScreen(
                navController = navController
            )
        }
    }
}