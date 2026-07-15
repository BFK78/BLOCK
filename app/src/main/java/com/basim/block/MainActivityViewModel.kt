package com.basim.block

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainActivityViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        MainActivityUiState(
            isUserAuthenticated = false,
            isOnBoardingCompleted = false,
        )
    )
    val uiState = _uiState.asStateFlow()
}

data class MainActivityUiState(
    val isUserAuthenticated: Boolean,
    val isOnBoardingCompleted: Boolean,
)
