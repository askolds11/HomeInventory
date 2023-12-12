package com.askolds.homeinventory.featureHome.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.askolds.homeinventory.featureHome.domain.usecase.HomeUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeUseCases: HomeUseCases,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    var state by mutableStateOf(HomeState())
        private set

    private val homeId = savedStateHandle.get<Int>("homeId")!!

    init {
        getState()
    }

    private fun getState() {
        viewModelScope.launch {
            homeUseCases.getFlow(homeId)
                .onEach {
                    state = state.copy(home = it)
                }
                .launchIn(viewModelScope)

        }
    }
}