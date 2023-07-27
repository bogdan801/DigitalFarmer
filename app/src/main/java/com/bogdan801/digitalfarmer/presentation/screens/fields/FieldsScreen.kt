package com.bogdan801.digitalfarmer.presentation.screens.fields

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.bogdan801.digitalfarmer.data.login.AuthUIClient
import com.bogdan801.digitalfarmer.presentation.navigation.Screen
import kotlinx.coroutines.launch

@Composable
fun FieldsScreen(
    navController: NavHostController,
    viewModel: FieldsViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val userData = remember{
        viewModel.authUIClient.getSignedInUser()
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        userData?.profilePictureUrl?.let { url ->
            AsyncImage(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape),
                model = url,
                contentDescription = "Profile picture",
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = userData?.username.toString())
        Text(text = userData?.userID.toString())
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                scope.launch {
                    viewModel.authUIClient.signOut()
                    Toast.makeText(context, "Signed out", Toast.LENGTH_SHORT).show()
                    navController.navigate(Screen.SignInScreen.route){
                        popUpTo(0)
                    }
                }
            }
        ) {
            Text(text = "Sign out")
        }
    }
}