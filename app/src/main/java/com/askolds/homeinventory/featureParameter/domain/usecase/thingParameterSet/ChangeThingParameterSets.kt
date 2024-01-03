package com.askolds.homeinventory.featureParameter.domain.usecase.thingParameterSet

import android.util.Log
import com.askolds.homeinventory.featureParameter.data.repository.ParameterSetParameterRepository
import com.askolds.homeinventory.featureParameter.data.repository.ThingParameterParameterSetRepository
import com.askolds.homeinventory.featureParameter.data.repository.ThingParameterSetRepository
import com.askolds.homeinventory.featureParameter.domain.model.ThingParameterSet
import com.askolds.homeinventory.featureParameter.domain.usecase.thingParameterParameterSet.ChangeThingParameterSetParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class ChangeThingParameterSets(
    private val repository: ThingParameterSetRepository,
    private val thingParameterParameterSetRepository: ThingParameterParameterSetRepository,
    private val parameterSetParameterRepository: ParameterSetParameterRepository
) {
    suspend operator fun invoke(thingId: Int, thingParameterSets: List<ThingParameterSet>) {
        return withContext(Dispatchers.IO) {
            val oldParameterSets = repository
                .getListByThingId(thingId)
                .first()

            // Get the removed parameter set ids
            // These are old parameters that do not show up in the new parameters
            val removedParameterSetIds = oldParameterSets
                .filterNot { oldParameterSet ->
                    thingParameterSets.any { thingParameterSet ->
                        oldParameterSet.id == thingParameterSet.parameterSetId
                    }
                }.map {
                    it.id
                }
            // Get the added parameter sets
            val addedParameterSets = thingParameterSets
                .filterNot { thingParameterSet ->
                    oldParameterSets.any { oldParameterSet ->
                        oldParameterSet.id == thingParameterSet.parameterSetId
                    }
                }.map {
                    it.toThingParameterSetEntity(
                        id = 0,
                        thingId = thingId
                    )
                }

            repository.deleteByThingIdAndSetIds(thingId, removedParameterSetIds)
            repository.insertAll(addedParameterSets)

            val insertedIds = repository.getIdsByThingId(thingId)

            // change thing parameter sets' parameters
            val changeThingParameterSetParameters = ChangeThingParameterSetParameters(
                thingParameterParameterSetRepository,
                parameterSetParameterRepository
            )
            thingParameterSets
                // remove removed parameter sets
                .filterNot {
                    removedParameterSetIds.contains(it.parameterSetId)
                }
                .map {thingParameterSet ->
                    val insertedId = insertedIds[thingParameterSet.parameterSetId]
                    thingParameterSet.copy(
                        thingParameters = thingParameterSet.thingParameters.map { thingParameter ->
                            thingParameter.copy (
                                thingParameterSetId = insertedId,
                            )
                        }
                    )
                }
                .forEach {
                    changeThingParameterSetParameters(
                        thingId = thingId,
                        thingParameterSet = it.copy ()
                    )
            }

        }
    }
}