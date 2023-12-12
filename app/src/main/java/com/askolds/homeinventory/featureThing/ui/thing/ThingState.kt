package com.askolds.homeinventory.featureThing.ui.thing

import com.askolds.homeinventory.featureThing.domain.model.Thing

data class ThingState (
    val thing: Thing = Thing(0, "", false, null, 0, imageId = null),
)