package com.askolds.homeinventory.featureThing.ui.thing

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.askolds.homeinventory.featureThing.domain.usecase.thing.ThingUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThingViewModel @Inject constructor(
    private val thingUseCases: ThingUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    var state by mutableStateOf(ThingState())
        private set

    private val thingId = savedStateHandle.get<Int>("thingId")!!

    init {
        getState()
    }

    private fun getState() {
        viewModelScope.launch {
            thingUseCases.getFlow(thingId)
                .onEach {
                    state = state.copy(thing = it)
                }
                .launchIn(viewModelScope)
        }
    }
}