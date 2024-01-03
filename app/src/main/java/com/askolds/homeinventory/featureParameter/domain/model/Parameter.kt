package com.askolds.homeinventory.featureParameter.domain.model

import com.askolds.homeinventory.domain.stripAccents
import com.askolds.homeinventory.featureParameter.data.model.ParameterEntity

data class Parameter(
    val id: Int = 0,
    val name: String,
) {
    internal fun toEntity(): ParameterEntity {
        return ParameterEntity(
            id,
            name.trim(),
            name.trim().stripAccents().lowercase(),
        )
    }
}

internal fun ParameterEntity.toParameter(): Parameter {
    return Parameter(id, name)
}



