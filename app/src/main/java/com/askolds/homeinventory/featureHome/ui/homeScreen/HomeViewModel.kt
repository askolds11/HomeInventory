package com.askolds.homeinventory.featureHome.ui.homeScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.askolds.homeinventory.featureHome.domain.usecase.home.HomeUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeUseCases: HomeUseCases,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    var state = HomeState()
        private set

    private val homeId = savedStateHandle.get<Int>("homeId")!!

    /**
     * Updates state.thing
     */
    val getHomeFlow: SharedFlow<Unit> = homeUseCases.getFlow(homeId)
        .map {
            state.home.value = it
            return@map //Unit
        }.shareIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000)
        )

    private fun getState() {
        viewModelScope.launch {
            getHomeFlow.collectLatest {
                cancel()
            }
        }
    }

    init {
        getState()
    }
}