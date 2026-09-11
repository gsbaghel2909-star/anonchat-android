package com.app.anonchat.core.network

import com.app.anonchat.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage
import javax.inject.Singleton

/**
 * Single Supabase client for the whole app, configured with the ANON key
 * only. Every privileged operation (matching, moderation) is enforced
 * server-side via RLS + Edge Functions — the anon key on its own can never
 * bypass row-level security, so it is safe to ship in the APK.
 *
 * The service-role key (which DOES bypass RLS) must never appear here or
 * anywhere in this app's source.
 */
@Module
@InstallIn(SingletonComponent::class)
object SupabaseModule {

    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient = createSupabaseClient(
        supabaseUrl = BuildConfig.SUPABASE_URL,
        supabaseKey = BuildConfig.SUPABASE_ANON_KEY
    ) {
        install(Auth) {
            // Persist session via an encrypted store, not plain SharedPreferences.
            // See core/security/EncryptedSessionStorage.kt
        }
        install(Postgrest)
        install(Realtime)
        install(Storage)
    }
}
