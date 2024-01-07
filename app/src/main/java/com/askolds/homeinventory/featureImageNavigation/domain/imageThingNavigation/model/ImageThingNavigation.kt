package com.askolds.homeinventory.featureImageNavigation.domain.imageThingNavigation.model

import com.askolds.homeinventory.featureImageNavigation.data.model.ImageThingNavigationEntity

data class ImageThingNavigation(
    val id: Int = 0,
    val thingId: Int?,
    val offsetX: Float,
    val offsetY: Float,
    val sizeX: Float,
    val sizeY: Float,
    val zIndex: Int,
    val selected: Boolean = false,
) {
    internal fun toEntity(imageId: Int): ImageThingNavigationEntity {
        return ImageThingNavigationEntity(
            id = id,
            imageId = imageId,
            thingId = thingId,
            offsetX = offsetX,
            offsetY = offsetY,
            sizeX = sizeX,
            sizeY = sizeY,
            zIndex = zIndex
        )
    }
}

internal fun ImageThingNavigationEntity.toImageThingNavigation(): ImageThingNavigation {
    return ImageThingNavigation(
        id, thingId, offsetX, offsetY, sizeX, sizeY, zIndex
    )
}