package com.bogdan801.digitalfarmer.presentation.screens.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bogdan801.digitalfarmer.data.datastore.saveStringToDataStore
import com.bogdan801.digitalfarmer.presentation.navigation.Screen

@Composable
fun SplashScreen(
    navController: NavHostController
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        context.saveStringToDataStore("show_splash_screen", "false")
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ){
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "Splash screen",
            fontSize = 36.sp
        )

        Button(
            modifier = Modifier.padding(16.dp),
            onClick = {
                navController.navigate(Screen.SignInScreen.route){
                    popUpTo(0)
                }
            }
        ) {
            Text(text = "Next")
        }
    }
}