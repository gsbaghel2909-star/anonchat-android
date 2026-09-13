package com.app.anonchat.core.di;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import io.ktor.client.HttpClient;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
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
public final class NetworkModule_ProvideFunctionsHttpClientFactory implements Factory<HttpClient> {
  @Override
  public HttpClient get() {
    return provideFunctionsHttpClient();
  }

  public static NetworkModule_ProvideFunctionsHttpClientFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static HttpClient provideFunctionsHttpClient() {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideFunctionsHttpClient());
  }

  private static final class InstanceHolder {
    private static final NetworkModule_ProvideFunctionsHttpClientFactory INSTANCE = new NetworkModule_ProvideFunctionsHttpClientFactory();
  }
}
