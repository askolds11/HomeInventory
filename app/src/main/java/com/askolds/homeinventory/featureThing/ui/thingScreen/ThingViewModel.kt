package com.askolds.homeinventory.featureThing.ui.thingScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.askolds.homeinventory.featureParameter.domain.usecase.thingParameterParameterSet.ThingParameterParameterSetUseCases
import com.askolds.homeinventory.featureThing.domain.usecase.thing.ThingUseCases
import com.askolds.homeinventory.featureThing.ui.formScreen.ThingFormEvent
import com.askolds.homeinventory.featureThing.ui.formScreen.toSnapshotThingParameterSet
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

@HiltViewModel
class ThingViewModel @Inject constructor(
    private val thingUseCases: ThingUseCases,
    private val thingParameterParameterSetUseCases: ThingParameterParameterSetUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val state = ThingState()

    private val thingId = savedStateHandle.get<Int>("thingId")!!

    /**
     * Updates state.thing
     */
    val getThingFlow: SharedFlow<Unit> = thingUseCases.getFlow(thingId)
        .map {
            state.thing.value = it
            return@map //Unit
        }.shareIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000)
        )

    /**
     * Updates state.thingParameters
     */
    val getThingParametersFlow: SharedFlow<Unit> = thingParameterParameterSetUseCases
        .getListWithoutSetByThingId(thingId).map {
            state.thingParameters.apply { clear() }.addAll(it)
            return@map //Unit
        }.shareIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000)
        )

    /**
     * Updates state.thingParameterSets
     */
    val getThingParameterSetsFlow: SharedFlow<Unit> = thingParameterParameterSetUseCases
        .getListGroupedBySetByThingId(thingId).map {
            state.thingParameterSets.apply { clear() }.addAll(
                it.map { thingParameterSet ->
                    thingParameterSet.toSnapshotThingParameterSet()
                }
            )
            return@map //Unit
        }.shareIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000)
        )
}