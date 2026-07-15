package com.basim.block.core.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.basim.block.core.navigation.main.MainNavigator

interface EntryProvider {
    fun register(
        scope: EntryProviderScope<NavKey>,
        navigator: Navigator
    )
    fun getType(): EntryProviderType
}

enum class EntryProviderType {
    AUTH_ENTRY_PROVIDER,
    MAIN_ENTRY_PROVIDER
}