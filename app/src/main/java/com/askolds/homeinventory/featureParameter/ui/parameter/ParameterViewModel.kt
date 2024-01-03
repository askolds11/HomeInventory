package com.askolds.homeinventory.featureParameter.ui.parameter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.askolds.homeinventory.featureParameter.domain.usecase.parameter.ParameterUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParameterViewModel @Inject constructor(
    private val parameterUseCases: ParameterUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    var state by mutableStateOf(ParameterState())
        private set

    private val parameterId = savedStateHandle.get<Int>("parameterId")!!

    init {
        getParameter()
    }

    private fun getParameter() {
        viewModelScope.launch {
            parameterUseCases.getFlow(parameterId)
                .onEach {
                    state = state.copy(parameter = it)
                }
                .launchIn(viewModelScope)
        }
    }
}