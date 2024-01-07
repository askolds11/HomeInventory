package com.askolds.homeinventory.featureThing.ui.thingScreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.askolds.homeinventory.featureParameter.domain.model.ThingParameter
import com.askolds.homeinventory.featureThing.domain.model.Thing
import com.askolds.homeinventory.featureThing.ui.formScreen.SnapshotThingParameterSet

@Stable
class ThingState (
    val thing: MutableState<Thing> = mutableStateOf(Thing(0, "", false, null, 0, imageId = null)),
    val thingParameters: SnapshotStateList<ThingParameter> = mutableStateListOf(),
    val thingParameterSets: SnapshotStateList<SnapshotThingParameterSet> = mutableStateListOf(),
)