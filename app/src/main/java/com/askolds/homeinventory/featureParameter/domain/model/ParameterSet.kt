package com.askolds.homeinventory.featureParameter.domain.model

import com.askolds.homeinventory.domain.stripAccents
import com.askolds.homeinventory.featureParameter.data.model.ParameterSetEntity

data class ParameterSet(
    val id: Int = 0,
    val name: String,
) {
    internal fun toEntity(): ParameterSetEntity {
        return ParameterSetEntity(
            id,
            name.trim(),
            name.trim().stripAccents().lowercase(),
        )
    }
}

internal fun ParameterSetEntity.toParameterSet(): ParameterSet {
    return ParameterSet(id, name)
}



