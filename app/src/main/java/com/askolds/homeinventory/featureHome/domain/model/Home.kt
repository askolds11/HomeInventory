package com.askolds.homeinventory.featureHome.domain.model

import com.askolds.homeinventory.domain.stripAccents
import com.askolds.homeinventory.featureHome.data.model.HomeEntity
import com.askolds.homeinventory.featureImage.domain.model.Image

data class Home(
    val id: Int = 0,
    val name: String,
    val image: Image? = null,
) {
    internal fun toEntity(): HomeEntity {
        return HomeEntity(
            id,
            name.trim(),
            name.trim().stripAccents().lowercase(),
            image?.id
        )
    }
}

internal fun HomeEntity.toHome(image: Image?): Home {
    return Home(id, name, image)
}



