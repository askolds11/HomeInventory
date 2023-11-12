package com.askolds.homeinventory.featureHome.domain.model

import com.askolds.homeinventory.featureHome.data.model.HomeEntity

data class HomeListItem(
    val id: Int,
    val name: String
)

internal fun HomeEntity.toHomeListItem(): HomeListItem {
    return HomeListItem(
        id, name
    )
}