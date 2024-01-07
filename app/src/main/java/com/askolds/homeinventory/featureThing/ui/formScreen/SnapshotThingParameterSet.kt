package com.askolds.homeinventory.featureThing.ui.formScreen

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import com.askolds.homeinventory.featureParameter.domain.model.ThingParameter
import com.askolds.homeinventory.featureParameter.domain.model.ThingParameterSet

data class SnapshotThingParameterSet(
    val parameterSetId: Int = 0,
    val parameterSetName: String,
    val thingParameters: SnapshotStateList<ThingParameter>,
) {
    internal fun toThingParameterSet(): ThingParameterSet {
        return ThingParameterSet(parameterSetId, parameterSetName, thingParameters)
    }
}

internal fun ThingParameterSet.toSnapshotThingParameterSet(): SnapshotThingParameterSet {
    return SnapshotThingParameterSet(parameterSetId, parameterSetName, thingParameters.toMutableStateList())
}