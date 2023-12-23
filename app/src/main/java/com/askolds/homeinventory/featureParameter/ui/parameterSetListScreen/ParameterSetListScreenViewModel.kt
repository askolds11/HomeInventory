package com.askolds.homeinventory.featureParameter.ui.parameterSetListScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.askolds.homeinventory.featureThing.domain.usecase.ThingUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ParameterSetListScreenViewModel @Inject constructor(
    private val thingUseCases: ThingUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
//    var state by mutableStateOf(TempState())
//        private set
//
//    private val thingId = savedStateHandle.get<Int>("thingId")!!
//
//    init {
//        getState()
//    }
//
//    private fun getState() {
//        viewModelScope.launch {
//            thingUseCases.getFlow(thingId)
//                .onEach {
//                    state = state.copy(thing = it)
//                }
//                .launchIn(viewModelScope)
//        }
//    }
}