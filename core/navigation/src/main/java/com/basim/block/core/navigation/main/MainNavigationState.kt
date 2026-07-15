package com.basim.block.core.navigation.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack

@Composable
fun rememberMainNavigationState(
    startKey: NavKey,
    topLevelKeys: Set<NavKey>
): MainNavigationState {
    val topLevelStack = rememberNavBackStack(startKey)
    val subStacks = topLevelKeys.associateWith { key -> rememberNavBackStack(key) }
    return remember(startKey, topLevelKeys) {
        MainNavigationState(
            startKey = startKey,
            topLevelStack = topLevelStack,
            subStacks = subStacks
        )
    }
}

class MainNavigationState(
    val startKey: NavKey,
    val topLevelStack: NavBackStack<NavKey>,
    val subStacks: Map<NavKey, NavBackStack<NavKey>>
) {

    val currentTopLevelKey: NavKey by derivedStateOf { topLevelStack.last() }

    val topLevelKeys
        get() = subStacks.keys

    val currentSubStack: NavBackStack<NavKey>
        get() = subStacks[currentTopLevelKey]
            ?: error("substack for $currentTopLevelKey dose not exist")

    val currentKey: NavKey by derivedStateOf { currentSubStack.last() }
}