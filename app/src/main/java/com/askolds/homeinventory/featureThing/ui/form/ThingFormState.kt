package com.askolds.homeinventory.featureThing.ui.form

import com.askolds.homeinventory.featureImage.domain.model.Image
import com.askolds.homeinventory.featureThing.domain.model.Thing
import com.askolds.homeinventory.featureThing.domain.usecase.validation.ValidateName
import com.askolds.homeinventory.ui.SaveStatus

data class ThingFormState (
    val thing: Thing = Thing(name = "", isContainer = false, parentId = null, homeId = 0, imageId = null),
    val image: Image? = null,
    val nameValidation: ValidateName.ERROR? = null,
    val saveEnabled: Boolean = false,
    val saveStatus: SaveStatus = SaveStatus.None,
)