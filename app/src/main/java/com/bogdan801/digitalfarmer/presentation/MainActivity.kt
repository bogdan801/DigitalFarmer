package com.bogdan801.digitalfarmer.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.bogdan801.digitalfarmer.data.login.AuthUIClient
import com.bogdan801.digitalfarmer.presentation.navigation.Navigation
import com.bogdan801.digitalfarmer.presentation.theme.DigitalFarmerTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var authUIClient: AuthUIClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DigitalFarmerTheme {
                //screens and navigation
                Navigation(
                    navController = rememberNavController(),
                    authUIClient = authUIClient
                )
            }
        }
    }
}
