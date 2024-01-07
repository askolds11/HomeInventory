package com.askolds.homeinventory.featureThing.domain.model

import com.askolds.homeinventory.core.domain.stripAccents
import com.askolds.homeinventory.featureThing.data.model.ThingEntity

data class Thing(
    val id: Int = 0,
    val name: String,
    val isContainer: Boolean,
    val parentId: Int?,
    val homeId: Int,
    val imageId: Int?,
    val imageUri: String? = null,
) {
    internal fun toEntity(): ThingEntity {
        return ThingEntity(
            id,
            name,
            name.stripAccents().lowercase(),
            isContainer,
            parentId,
            homeId,
            imageId
        )
    }
}

internal fun ThingEntity.toThing(imageUri: String? = null): Thing {
    return Thing(id, name, isContainer, parentId, homeId, imageId, imageUri)
}



