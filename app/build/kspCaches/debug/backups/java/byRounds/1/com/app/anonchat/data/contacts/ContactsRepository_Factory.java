package com.app.anonchat.data.contacts;

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
public final class ContactsRepository_Factory implements Factory<ContactsRepository> {
  private final Provider<SupabaseClient> supabaseClientProvider;

  public ContactsRepository_Factory(Provider<SupabaseClient> supabaseClientProvider) {
    this.supabaseClientProvider = supabaseClientProvider;
  }

  @Override
  public ContactsRepository get() {
    return newInstance(supabaseClientProvider.get());
  }

  public static ContactsRepository_Factory create(Provider<SupabaseClient> supabaseClientProvider) {
    return new ContactsRepository_Factory(supabaseClientProvider);
  }

  public static ContactsRepository newInstance(SupabaseClient supabaseClient) {
    return new ContactsRepository(supabaseClient);
  }
}
