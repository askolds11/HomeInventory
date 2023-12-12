package com.askolds.homeinventory.featureImage.domain.model

import com.askolds.homeinventory.featureImage.data.model.ImageEntity

data class Image(
    val id: Int = 0,
    val fileName: String,
    val imageUri: String
) {
    internal fun toEntity(): ImageEntity {
        return ImageEntity(
            id,
            fileName,
            imageUri
        )
    }
}

internal fun ImageEntity.toImage(): Image {
    return Image(id, fileName, imageUri)
}
