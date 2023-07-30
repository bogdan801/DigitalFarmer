package com.bogdan801.digitalfarmer.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bogdan801.digitalfarmer.data.datastore.readStringFromDataStore
import com.bogdan801.digitalfarmer.data.login.AuthUIClient
import com.bogdan801.digitalfarmer.presentation.screens.fields.FieldsScreen
import com.bogdan801.digitalfarmer.presentation.screens.authentication.recover_password.RecoverPasswordScreen
import com.bogdan801.digitalfarmer.presentation.screens.authentication.register.RegisterScreen
import com.bogdan801.digitalfarmer.presentation.screens.authentication.sign_in.SignInScreen
import com.bogdan801.digitalfarmer.presentation.screens.authentication.splash.SplashScreen
import kotlinx.coroutines.runBlocking

@Composable
fun Navigation(
    navController: NavHostController,
    authUIClient: AuthUIClient
) {
    val context = LocalContext.current
    val startDestination =
        if(authUIClient.getSignedInUser() != null) Screen.FieldsScreen.route
        else {
            val showSplashScreen: Boolean? = runBlocking {
                context.readStringFromDataStore("show_splash_screen")?.toBooleanStrictOrNull()
            }
            if (showSplashScreen == null) Screen.SplashScreen.route
            else Screen.SignInScreen.route
        }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ){
        composable(Screen.SplashScreen.route){
            SplashScreen(navController = navController)
        }

        composable(Screen.SignInScreen.route){
            SignInScreen(navController = navController)
        }

        composable(Screen.RecoverPasswordScreen.route){
            RecoverPasswordScreen(navController = navController)
        }

        composable(Screen.RegisterScreen.route){
            RegisterScreen(navController = navController)
        }

        composable(Screen.FieldsScreen.route){
            FieldsScreen(navController = navController)
        }
    }
}