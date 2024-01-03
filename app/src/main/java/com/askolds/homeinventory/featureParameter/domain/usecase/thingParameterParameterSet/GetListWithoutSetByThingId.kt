package com.askolds.homeinventory.featureParameter.domain.usecase.thingParameterParameterSet

import com.askolds.homeinventory.featureParameter.data.repository.ThingParameterParameterSetRepository
import com.askolds.homeinventory.featureParameter.domain.model.ThingParameter
import com.askolds.homeinventory.featureParameter.domain.model.toThingParameter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetListWithoutSetByThingId(
    private val repository: ThingParameterParameterSetRepository,
) {
    operator fun invoke(thingId: Int): Flow<List<ThingParameter>> {
        return repository.getListWithoutSetByThingId(thingId).map { items ->
            items.map { item ->
                item.toThingParameter()
            }
        }
    }
}