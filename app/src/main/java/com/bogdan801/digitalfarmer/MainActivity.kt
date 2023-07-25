package com.bogdan801.digitalfarmer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.bogdan801.digitalfarmer.presentation.login.GoogleAuthUIClient
import com.bogdan801.digitalfarmer.presentation.navigation.Navigation
import com.bogdan801.digitalfarmer.presentation.theme.DigitalFarmerTheme
import com.google.android.gms.auth.api.identity.Identity

class MainActivity : ComponentActivity() {
    val googleAuthUIClient by lazy {
        GoogleAuthUIClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DigitalFarmerTheme {
                //screens and navigation
                Navigation(
                    navController = rememberNavController(),
                    googleAuthUIClient = googleAuthUIClient
                )
            }
        }
    }
}
