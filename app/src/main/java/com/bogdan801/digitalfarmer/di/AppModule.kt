package com.bogdan801.digitalfarmer.di

import android.content.Context
import com.bogdan801.digitalfarmer.BuildConfig
import com.bogdan801.digitalfarmer.data.login.AuthUIClient
import com.bogdan801.digitalfarmer.data.repository.RepositoryImpl
import com.bogdan801.digitalfarmer.domain.repository.Repository
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): BaseApplication {
        return app as BaseApplication
    }

    @Singleton
    @Provides
    fun provideAuthUIClient(@ApplicationContext app: Context): AuthUIClient {
        return AuthUIClient(
            context = app,
            oneTapClient = Identity.getSignInClient(app)
        )
    }

    @Singleton
    @Provides
    fun provideRealtimeDatabase(@ApplicationContext app: Context): DatabaseReference {
        return Firebase.database("https://digitalfarmer-6f2c7-default-rtdb.europe-west1.firebasedatabase.app/").reference
    }

    @Provides
    @Singleton
    fun provideRepository(db: DatabaseReference, client: AuthUIClient): Repository {
        return RepositoryImpl(db, client)
    }
}