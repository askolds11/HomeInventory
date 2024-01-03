package com.askolds.homeinventory.featureParameter.domain.usecase.thingParameterParameterSet

import com.askolds.homeinventory.featureParameter.data.repository.ParameterSetParameterRepository
import com.askolds.homeinventory.featureParameter.data.repository.ThingParameterParameterSetRepository
import com.askolds.homeinventory.featureParameter.domain.model.ThingParameterSet
import com.askolds.homeinventory.featureParameter.domain.model.toThingParameter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ChangeThingParameterSetParameters (
    private val repository: ThingParameterParameterSetRepository,
    private val parameterSetParameterRepository: ParameterSetParameterRepository
) {
    suspend operator fun invoke(thingId: Int, thingParameterSet: ThingParameterSet) {
        return withContext(Dispatchers.IO) {
            val oldThingParameters = repository
                .getListByThingIdAndSetId(thingId, thingParameterSet.parameterSetId)
                .first()
                .map {
                    it.toThingParameter()
                }

            val thingParameters = thingParameterSet.thingParameters

            val parameterSetParametersIds = parameterSetParameterRepository
                .getIdsBySetId(thingParameterSet.parameterSetId)

            // Get the removed thing parameter ids
            // These are old thing parameters that do not show up in the new thing parameters
            val removedParameterIds = oldThingParameters
                .filterNot {oldThingParameter ->
                    thingParameters.any { thingParameter ->
                        oldThingParameter.id == thingParameter.id
                    }
                }.map {
                    it.id
                }
            // Get the updated parameters
            val updatedParameters = thingParameters
                .filter {
                    it.id != 0
                }.map {
                    it.copy(
                        parameterSetParameterId = parameterSetParametersIds[it.parameterId]
                    ).toEntity(thingId)
                }
            // Get the added parameters
            val addedParameters = thingParameters
                .filter {
                    it.id == 0
                }.map {
                    it.copy(
                        parameterSetParameterId = parameterSetParametersIds[it.parameterId]
                    )
                }.map {
                    it.toEntity(thingId)
                }

            repository.deleteByIds(removedParameterIds)
            repository.updateAll(updatedParameters)
            repository.insertAll(addedParameters)
        }
    }
}