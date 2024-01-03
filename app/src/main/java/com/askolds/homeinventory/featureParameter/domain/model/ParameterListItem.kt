package com.askolds.homeinventory.featureParameter.domain.model

import com.askolds.homeinventory.featureParameter.data.model.ParameterEntity

data class ParameterListItem(
    val id: Int,
    val name: String,
    val selected: Boolean = false,
)

internal fun ParameterEntity.toParameterListItem(selected: Boolean = false): ParameterListItem {
    return ParameterListItem(
        id = id,
        name = name,
        selected = selected
    )
}