package com.askolds.homeinventory.featureParameter.domain.model

import com.askolds.homeinventory.featureParameter.data.dao.ThingParameterSetDataEntry
import com.askolds.homeinventory.featureParameter.data.model.ThingParameterSetEntity

data class ThingParameterSet(
    val parameterSetId: Int,
    val parameterSetName: String,
    val thingParameters: List<ThingParameter>,
) {
    internal fun toThingParameterSetEntity(id: Int, thingId: Int): ThingParameterSetEntity {
        return ThingParameterSetEntity(
            id = id,
            thingId = thingId,
            parameterSetId = parameterSetId
        )
    }
}

internal fun ThingParameterSetDataEntry.toThingParameterSet(): ThingParameterSet {
    val parameterSetEntity = this.key
    val thingParameterParameterSets = this.value

    return ThingParameterSet(
        parameterSetEntity.id,
        parameterSetEntity.name,
        thingParameterParameterSets.map {
            it.toThingParameter(parameterSetEntity.id)
        }
    )
}



