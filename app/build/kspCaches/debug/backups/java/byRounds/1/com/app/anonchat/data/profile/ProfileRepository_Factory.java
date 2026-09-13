package com.app.anonchat.data.profile;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import io.github.jan.supabase.SupabaseClient;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class ProfileRepository_Factory implements Factory<ProfileRepository> {
  private final Provider<SupabaseClient> supabaseClientProvider;

  public ProfileRepository_Factory(Provider<SupabaseClient> supabaseClientProvider) {
    this.supabaseClientProvider = supabaseClientProvider;
  }

  @Override
  public ProfileRepository get() {
    return newInstance(supabaseClientProvider.get());
  }

  public static ProfileRepository_Factory create(Provider<SupabaseClient> supabaseClientProvider) {
    return new ProfileRepository_Factory(supabaseClientProvider);
  }

  public static ProfileRepository newInstance(SupabaseClient supabaseClient) {
    return new ProfileRepository(supabaseClient);
  }
}
