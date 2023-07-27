package com.bogdan801.digitalfarmer.data.login

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.widget.Toast
import com.bogdan801.digitalfarmer.R
import com.bogdan801.digitalfarmer.domain.model.User
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await

class AuthUIClient(
    private val context: Context,
    private val oneTapClient: SignInClient
) {
    private val auth = Firebase.auth

    suspend fun getSignInWithGoogleIntentSender(): IntentSender? {
        val result = try {
            oneTapClient.beginSignIn(
                buildGoogleSignInRequest()
            ).await()
        }
        catch (e: Exception){
            e.printStackTrace()
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            if(e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }

    private fun buildGoogleSignInRequest(): BeginSignInRequest
        = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(context.getString(R.string.web_client_id))
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()


    suspend fun signInWithIntent(intent: Intent): SignInResult {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)

        return try {
            val user = auth.signInWithCredential(googleCredentials).await().user
            SignInResult(
                userData = user?.run {
                    User(
                        userID = uid,
                        username = displayName,
                        profilePictureUrl = photoUrl?.toString()
                    )
                }
            )
        }
        catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            if(e is CancellationException) throw e
            SignInResult(
                userData = null,
                errorMessage = e.message
            )
        }
    }

    suspend fun createUserAndSignIn(email: String, password: String): SignInResult {
        try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            return SignInResult(
                userData = User(
                    userID = result.user?.uid.toString(),
                    username = result.user?.displayName,
                    profilePictureUrl = result.user?.photoUrl.toString()
                )
            )
        }
        catch (e: IllegalArgumentException){
            e.printStackTrace()
            return SignInResult(
                userData = null,
                errorMessage = e.message,
                errorType = ErrorType.WrongEmailFormat
            )
        }
        catch (e: FirebaseAuthWeakPasswordException){
            e.printStackTrace()
            return SignInResult(
                userData = null,
                errorMessage = e.message,
                errorType = ErrorType.WeakPassword
            )
        }
        catch (e: FirebaseAuthUserCollisionException){
            e.printStackTrace()
            return SignInResult(
                userData = null,
                errorMessage = e.message,
                errorType = ErrorType.AccountAlreadyExists
            )
        }
        catch (e: Exception){
            e.printStackTrace()
            if(e is CancellationException) throw e
            return SignInResult(
                userData = null,
                errorMessage = e.message,
                errorType = ErrorType.Other
            )
        }
    }

    suspend fun signInWithEmailAndPassword(email: String, password: String): SignInResult {
        try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            return SignInResult(
                userData = User(
                    userID = result.user?.uid.toString(),
                    username = result.user?.displayName,
                    profilePictureUrl = result.user?.photoUrl.toString()
                )
            )
        }
        catch (e: FirebaseAuthInvalidCredentialsException){
            e.printStackTrace()
            return SignInResult(
                userData = null,
                errorMessage = e.message,
                errorType = ErrorType.WrongEmailOrPassWord
            )
        }
        catch (e: Exception){
            e.printStackTrace()
            if(e is CancellationException) throw e
            return SignInResult(
                userData = null,
                errorMessage = e.message
            )
        }
    }

    suspend fun signOut(){
        try {
            oneTapClient.signOut().await()
            auth.signOut()
        }
        catch (e: Exception){
            e.printStackTrace()
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            if(e is CancellationException) throw e
        }
    }

    fun getSignedInUser(): User? = auth.currentUser?.run {
        User(
            userID = uid,
            username = displayName,
            photoUrl?.toString()
        )
    }
}