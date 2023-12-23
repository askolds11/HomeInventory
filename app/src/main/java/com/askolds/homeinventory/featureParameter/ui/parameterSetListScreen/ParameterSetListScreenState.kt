package com.askolds.homeinventory.featureParameter.ui.parameterSetListScreen

import com.askolds.homeinventory.featureThing.domain.model.Thing

data class ParameterSetListScreenState (
    val thing: Thing = Thing(0, "", false, null, 0, imageId = null),
)