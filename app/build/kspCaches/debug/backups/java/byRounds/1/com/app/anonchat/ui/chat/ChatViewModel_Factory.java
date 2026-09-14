package com.app.anonchat.ui.chat;

import androidx.lifecycle.SavedStateHandle;
import com.app.anonchat.data.chat.ChatRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class ChatViewModel_Factory implements Factory<ChatViewModel> {
  private final Provider<ChatRepository> chatRepositoryProvider;

  private final Provider<SavedStateHandle> savedStateHandleProvider;

  public ChatViewModel_Factory(Provider<ChatRepository> chatRepositoryProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    this.chatRepositoryProvider = chatRepositoryProvider;
    this.savedStateHandleProvider = savedStateHandleProvider;
  }

  @Override
  public ChatViewModel get() {
    return newInstance(chatRepositoryProvider.get(), savedStateHandleProvider.get());
  }

  public static ChatViewModel_Factory create(Provider<ChatRepository> chatRepositoryProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    return new ChatViewModel_Factory(chatRepositoryProvider, savedStateHandleProvider);
  }

  public static ChatViewModel newInstance(ChatRepository chatRepository,
      SavedStateHandle savedStateHandle) {
    return new ChatViewModel(chatRepository, savedStateHandle);
  }
}
