package com.app.anonchat.data.chat;

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
public final class ChatRepository_Factory implements Factory<ChatRepository> {
  private final Provider<SupabaseClient> supabaseClientProvider;

  public ChatRepository_Factory(Provider<SupabaseClient> supabaseClientProvider) {
    this.supabaseClientProvider = supabaseClientProvider;
  }

  @Override
  public ChatRepository get() {
    return newInstance(supabaseClientProvider.get());
  }

  public static ChatRepository_Factory create(Provider<SupabaseClient> supabaseClientProvider) {
    return new ChatRepository_Factory(supabaseClientProvider);
  }

  public static ChatRepository newInstance(SupabaseClient supabaseClient) {
    return new ChatRepository(supabaseClient);
  }
}
