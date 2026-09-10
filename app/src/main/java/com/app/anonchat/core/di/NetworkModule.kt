package com.app.anonchat.core.di

import com.app.anonchat.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideFunctionsHttpClient(): HttpClient = HttpClient(Android)

    @Provides
    @Singleton
    @Named("edgeFunctionsBaseUrl")
    fun provideEdgeFunctionsBaseUrl(): String =
        "${BuildConfig.SUPABASE_URL}/functions/v1"
}
