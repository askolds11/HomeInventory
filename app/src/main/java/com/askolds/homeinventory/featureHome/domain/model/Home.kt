package com.askolds.homeinventory.featureHome.domain.model

import com.askolds.homeinventory.featureHome.data.model.HomeEntity
import com.askolds.homeinventory.ui.stripAccents

data class Home(
    val id: Int = 0,
    val name: String
) {
    internal fun toEntity(): HomeEntity {
        return HomeEntity(
            id,
            name,
            name.stripAccents().lowercase()
        )
    }
}

internal fun HomeEntity.toHome(): Home {
    return Home(id, name)
}



