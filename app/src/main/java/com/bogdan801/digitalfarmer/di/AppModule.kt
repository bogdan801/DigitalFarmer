package com.bogdan801.digitalfarmer.di

import android.content.Context
import com.bogdan801.digitalfarmer.presentation.login.GoogleAuthUIClient
import com.google.android.gms.auth.api.identity.Identity
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
    fun provideGoogleAuthUIClient(@ApplicationContext app: Context): GoogleAuthUIClient {
        return GoogleAuthUIClient(
            context = app,
            oneTapClient = Identity.getSignInClient(app)
        )
    }
}