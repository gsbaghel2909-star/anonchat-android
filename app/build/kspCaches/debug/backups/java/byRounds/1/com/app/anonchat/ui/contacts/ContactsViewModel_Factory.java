package com.app.anonchat.ui.contacts;

import com.app.anonchat.data.chat.ChatRepository;
import com.app.anonchat.data.contacts.ContactsRepository;
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
public final class ContactsViewModel_Factory implements Factory<ContactsViewModel> {
  private final Provider<ContactsRepository> contactsRepositoryProvider;

  private final Provider<ChatRepository> chatRepositoryProvider;

  public ContactsViewModel_Factory(Provider<ContactsRepository> contactsRepositoryProvider,
      Provider<ChatRepository> chatRepositoryProvider) {
    this.contactsRepositoryProvider = contactsRepositoryProvider;
    this.chatRepositoryProvider = chatRepositoryProvider;
  }

  @Override
  public ContactsViewModel get() {
    return newInstance(contactsRepositoryProvider.get(), chatRepositoryProvider.get());
  }

  public static ContactsViewModel_Factory create(
      Provider<ContactsRepository> contactsRepositoryProvider,
      Provider<ChatRepository> chatRepositoryProvider) {
    return new ContactsViewModel_Factory(contactsRepositoryProvider, chatRepositoryProvider);
  }

  public static ContactsViewModel newInstance(ContactsRepository contactsRepository,
      ChatRepository chatRepository) {
    return new ContactsViewModel(contactsRepository, chatRepository);
  }
}
