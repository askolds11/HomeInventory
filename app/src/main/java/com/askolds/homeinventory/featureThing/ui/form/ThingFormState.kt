package com.askolds.homeinventory.featureThing.ui.form

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.askolds.homeinventory.featureImage.domain.model.Image
import com.askolds.homeinventory.featureParameter.domain.model.ThingParameter
import com.askolds.homeinventory.featureThing.domain.model.Thing
import com.askolds.homeinventory.featureThing.domain.usecase.thing.validation.ValidateName
import com.askolds.homeinventory.ui.SaveStatus

data class ThingFormState (
    val isEditMode: Boolean = false,
    val thing: Thing = Thing(name = "", isContainer = false, parentId = null, homeId = 0, imageId = null),
    val image: Image? = null,
    val nameValidation: ValidateName.ERROR? = null,
    val thingParameters: SnapshotStateList<ThingParameter> = mutableStateListOf(),
    val thingParameterSets: SnapshotStateList<SnapshotThingParameterSet> = mutableStateListOf(),
    val saveEnabled: Boolean = false,
    val saveStatus: SaveStatus = SaveStatus.None,
)