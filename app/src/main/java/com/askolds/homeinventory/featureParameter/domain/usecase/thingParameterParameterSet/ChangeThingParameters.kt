package com.askolds.homeinventory.featureParameter.domain.usecase.thingParameterParameterSet

import com.askolds.homeinventory.featureParameter.data.repository.ThingParameterParameterSetRepository
import com.askolds.homeinventory.featureParameter.domain.model.ThingParameter
import com.askolds.homeinventory.featureParameter.domain.model.toThingParameter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class ChangeThingParameters (
    private val repository: ThingParameterParameterSetRepository,
) {
    suspend operator fun invoke(thingId: Int, thingParameters: List<ThingParameter>) {
        return withContext(Dispatchers.IO) {
            val oldThingParameters = repository
                .getListWithoutSetByThingId(thingId)
                .first()
                .map { item ->
                    item.toThingParameter()
                }

            // Get the removed parameter ids
            // These are old parameters that do not show up in the new parameters
            val removedParameterIds = oldThingParameters
                .filterNot {oldParameter ->
                    thingParameters.any { thingParameter ->
                        oldParameter.id == thingParameter.id
                    }
                }.map {
                    it.id
                }
            // Get the updated parameters
            val updatedParameters = thingParameters
                .filter {
                    it.id != 0
                }.map {
                    it.toEntity(thingId)
                }
            // Get the added parameters
            val addedParameters = thingParameters
                .filter {
                    it.id == 0
                }.map {
                    it.toEntity(thingId)
                }

            repository.deleteByIds(removedParameterIds)
            repository.updateAll(updatedParameters)
            repository.insertAll(addedParameters)
        }
    }
}