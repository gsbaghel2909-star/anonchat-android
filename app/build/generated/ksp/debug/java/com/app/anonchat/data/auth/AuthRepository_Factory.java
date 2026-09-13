package com.app.anonchat.data.auth;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import io.github.jan.supabase.SupabaseClient;
import io.ktor.client.HttpClient;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("javax.inject.Named")
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class AuthRepository_Factory implements Factory<AuthRepository> {
  private final Provider<SupabaseClient> supabaseProvider;

  private final Provider<HttpClient> functionsHttpClientProvider;

  private final Provider<String> edgeFunctionsBaseUrlProvider;

  public AuthRepository_Factory(Provider<SupabaseClient> supabaseProvider,
      Provider<HttpClient> functionsHttpClientProvider,
      Provider<String> edgeFunctionsBaseUrlProvider) {
    this.supabaseProvider = supabaseProvider;
    this.functionsHttpClientProvider = functionsHttpClientProvider;
    this.edgeFunctionsBaseUrlProvider = edgeFunctionsBaseUrlProvider;
  }

  @Override
  public AuthRepository get() {
    return newInstance(supabaseProvider.get(), functionsHttpClientProvider.get(), edgeFunctionsBaseUrlProvider.get());
  }

  public static AuthRepository_Factory create(Provider<SupabaseClient> supabaseProvider,
      Provider<HttpClient> functionsHttpClientProvider,
      Provider<String> edgeFunctionsBaseUrlProvider) {
    return new AuthRepository_Factory(supabaseProvider, functionsHttpClientProvider, edgeFunctionsBaseUrlProvider);
  }

  public static AuthRepository newInstance(SupabaseClient supabase, HttpClient functionsHttpClient,
      String edgeFunctionsBaseUrl) {
    return new AuthRepository(supabase, functionsHttpClient, edgeFunctionsBaseUrl);
  }
}
