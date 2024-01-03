package com.askolds.homeinventory.featureParameter.domain.model

import com.askolds.homeinventory.featureParameter.data.dao.ThingParameterDataEntry
import com.askolds.homeinventory.featureParameter.data.model.ThingParameterParameterSetEntity

data class ThingParameter(
    val id: Int = 0,
    val value: String,
    val parameterId: Int,
    val parameterSetId: Int?,
    val parameterSetParameterId: Int?,
    val thingParameterSetId: Int?,
    val parameterName: String,
) {
    internal fun toEntity(thingId: Int): ThingParameterParameterSetEntity {
        return ThingParameterParameterSetEntity(
            id = id,
            value = value.trim(),
            thingId = thingId,
            parameterId = parameterId,
            parameterSetParameterId = parameterSetParameterId,
            thingParameterSetId = thingParameterSetId
        )
    }
}

internal fun ThingParameterDataEntry.toThingParameter(
    parameterSetId: Int? = null
): ThingParameter {
    val parameter = this.value
    with (this.key) {
        return ThingParameter(
            id = id,
            value = value,
            parameterId = parameter.id,
            parameterSetId = parameterSetId,
            parameterSetParameterId = parameterSetParameterId,
            thingParameterSetId = thingParameterSetId,
            parameterName = parameter.name
        )
    }

}



