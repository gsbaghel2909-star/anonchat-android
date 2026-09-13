package com.app.anonchat.core.di;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class NetworkModule_ProvideEdgeFunctionsBaseUrlFactory implements Factory<String> {
  @Override
  public String get() {
    return provideEdgeFunctionsBaseUrl();
  }

  public static NetworkModule_ProvideEdgeFunctionsBaseUrlFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static String provideEdgeFunctionsBaseUrl() {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideEdgeFunctionsBaseUrl());
  }

  private static final class InstanceHolder {
    private static final NetworkModule_ProvideEdgeFunctionsBaseUrlFactory INSTANCE = new NetworkModule_ProvideEdgeFunctionsBaseUrlFactory();
  }
}
