package com.basim.block.core.navigation.main

import androidx.navigation3.runtime.NavKey

// TODO:: Find a way to inherit Navigator
class MainNavigator(val mainNavigationState: MainNavigationState) {

    fun navigate(key: NavKey) {
        when (key) {
            mainNavigationState.currentTopLevelKey -> clearSubStack()
            in mainNavigationState.topLevelKeys -> goToTopLevel(key)
            else -> goToKey(key)
        }
    }

    fun goBack() {
        when (mainNavigationState.currentKey) {
            mainNavigationState.startKey -> error("You cannot go back from the start route")
            mainNavigationState.currentTopLevelKey -> {
                // We're at the base of the current sub stack, go back to the previous top level
                // stack.
                mainNavigationState.topLevelStack.removeLastOrNull()
            }
            else -> mainNavigationState.currentSubStack.removeLastOrNull()
        }
    }

    /**
     * Go to a non top level key.
     */
    private fun goToKey(key: NavKey) {
        mainNavigationState.currentSubStack.apply {
            // Remove it if it's already in the stack so it's added at the end.
            remove(key)
            add(key)
        }
    }

    /**
     * Go to a top level stack.
     */
    private fun goToTopLevel(key: NavKey) {
        mainNavigationState.topLevelStack.apply {
            if (key == mainNavigationState.startKey) {
                // This is the start key. Clear the stack so it's added as the only key.
                clear()
            } else {
                // Remove it if it's already in the stack so it's added at the end.
                remove(key)
            }
            add(key)
        }
    }

    /**
     * Clearing all but the root key in the current sub stack.
     */
    private fun clearSubStack() {
        mainNavigationState.currentSubStack.run {
            if (size > 1) subList(1, size).clear()
        }
    }
}