package com.askolds.homeinventory.featureThing.domain.model

import android.net.Uri
import com.askolds.homeinventory.featureThing.data.model.ThingEntity

data class ThingListItem(
    val id: Int,
    val homeId: Int,
    val name: String,
    val isContainer: Boolean,
    val selected: Boolean = false,
    val imageUri: String? = null
)

internal fun ThingEntity.toThingListItem(imageUri: String? = null): ThingListItem {
    return ThingListItem(
        id = id,
        homeId = homeId,
        name = name,
        isContainer = isContainer,
        imageUri = imageUri
    )
}