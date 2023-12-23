package com.askolds.homeinventory.featureParameter.ui.parameterSet

import com.askolds.homeinventory.featureThing.domain.model.Thing

data class TempState (
    val thing: Thing = Thing(0, "", false, null, 0, imageId = null),
)