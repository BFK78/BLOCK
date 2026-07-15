package com.basim.block.features.authentication.navigation

import com.basim.block.core.navigation.EntryProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class AuthEntryProviderModule {
    @Binds
    @IntoSet
    abstract fun bindAuthEntryProvider(impl: AuthEntryProvider): EntryProvider
}