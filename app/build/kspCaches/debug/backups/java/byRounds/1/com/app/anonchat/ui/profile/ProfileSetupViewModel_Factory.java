package com.app.anonchat.ui.profile;

import com.app.anonchat.data.profile.ProfileRepository;
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
public final class ProfileSetupViewModel_Factory implements Factory<ProfileSetupViewModel> {
  private final Provider<ProfileRepository> profileRepositoryProvider;

  public ProfileSetupViewModel_Factory(Provider<ProfileRepository> profileRepositoryProvider) {
    this.profileRepositoryProvider = profileRepositoryProvider;
  }

  @Override
  public ProfileSetupViewModel get() {
    return newInstance(profileRepositoryProvider.get());
  }

  public static ProfileSetupViewModel_Factory create(
      Provider<ProfileRepository> profileRepositoryProvider) {
    return new ProfileSetupViewModel_Factory(profileRepositoryProvider);
  }

  public static ProfileSetupViewModel newInstance(ProfileRepository profileRepository) {
    return new ProfileSetupViewModel(profileRepository);
  }
}
