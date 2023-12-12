package com.askolds.homeinventory.featureHome.domain.model

import com.askolds.homeinventory.featureHome.data.model.HomeEntity

data class HomeListItem(
    val id: Int,
    val name: String,
    val selected: Boolean = false,
    val imageUri: String? = null
)

internal fun HomeEntity.toHomeListItem(imageUri: String?): HomeListItem {
    return HomeListItem(
        id = id,
        name = name,
        imageUri = imageUri
    )
}