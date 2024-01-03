package com.askolds.homeinventory.featureParameter.ui.parameterSet

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.askolds.homeinventory.featureParameter.domain.usecase.parameterSet.ParameterSetUseCases
import com.askolds.homeinventory.featureParameter.domain.usecase.parameterSetParameter.ParameterSetParameterUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParameterSetViewModel @Inject constructor(
    private val parameterSetUseCases: ParameterSetUseCases,
    private val parameterSetParameterUseCases: ParameterSetParameterUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    var state by mutableStateOf(ParameterSetState())
        private set

    private val parameterSetId = savedStateHandle.get<Int>("parameterSetId")!!

    init {
        getParameterSet()
        getParameters()
    }

    private fun getParameterSet() {
        viewModelScope.launch {
            parameterSetUseCases.getFlow(parameterSetId)
                .onEach {
                    state = state.copy(parameterSet = it)
                }
                .launchIn(viewModelScope)
        }
    }

    private fun getParameters() {
        viewModelScope.launch {
            parameterSetParameterUseCases
                .getFlowParameterList(parameterSetId)
                .onEach {
                    state = state.copy(parameters = it)
                }
                .launchIn(viewModelScope)

        }
    }
}