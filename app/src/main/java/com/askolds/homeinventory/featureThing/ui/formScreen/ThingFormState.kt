package com.askolds.homeinventory.featureThing.ui.formScreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.askolds.homeinventory.featureImage.domain.model.Image
import com.askolds.homeinventory.featureParameter.domain.model.ThingParameter
import com.askolds.homeinventory.featureThing.domain.model.Thing
import com.askolds.homeinventory.featureThing.domain.usecase.thing.validation.ValidateName
import com.askolds.homeinventory.core.ui.SaveStatus

@Stable
class ThingFormState (
    val isEditMode: MutableState<Boolean> = mutableStateOf(false),
    val thing: MutableState<Thing> = mutableStateOf(Thing(name = "", isContainer = false, parentId = null, homeId = 0, imageId = null)),
    val image: MutableState<Image?> = mutableStateOf(null),
    val nameValidation: MutableState<ValidateName.ERROR?> = mutableStateOf(null),
    val thingParameters: SnapshotStateList<ThingParameter> = mutableStateListOf(),
    val thingParameterSets: SnapshotStateList<SnapshotThingParameterSet> = mutableStateListOf(),
    val saveEnabled: MutableState<Boolean> = mutableStateOf(false),
    val saveStatus: MutableState<SaveStatus> = mutableStateOf(SaveStatus.None),
)