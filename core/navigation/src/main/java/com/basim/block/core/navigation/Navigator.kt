package com.basim.block.core.navigation

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.NavKey

interface Navigator {
    val backStack: SnapshotStateList<NavKey>
    fun goTo(key: NavKey)
    fun goBack(count: Int = 1)
    fun replaceAll(key: NavKey)
    fun popUpTo(key: NavKey, inclusive: Boolean = false)
}