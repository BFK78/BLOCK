package com.basim.block.core.navigation.auth

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.NavKey
import com.basim.block.core.navigation.Navigator

class AuthNavigator(startKey: NavKey): Navigator {

    override val backStack: SnapshotStateList<NavKey> = mutableStateListOf(startKey)

    override fun goTo(key: NavKey) {
        if (backStack.lastOrNull() == key) return
        backStack.add(key)
    }

    override fun goBack(count: Int) {
        val poppable = (backStack.size - 1).coerceAtLeast(0)
        repeat(count.coerceAtMost(poppable)) { backStack.removeAt(backStack.lastIndex) }
    }

    override fun replaceAll(key: NavKey) {
        backStack.clear()
        backStack.add(key)
    }

    override fun popUpTo(key: NavKey, inclusive: Boolean) {
        val index = backStack.indexOfLast { it == key }
        if (index == -1) return
        val targetSize = (if (inclusive) index else index + 1).coerceAtLeast(1)
        while (backStack.size > targetSize) backStack.removeAt(backStack.lastIndex)
    }
}