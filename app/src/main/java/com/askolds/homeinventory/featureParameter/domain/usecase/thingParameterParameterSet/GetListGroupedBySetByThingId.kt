package com.askolds.homeinventory.featureParameter.domain.usecase.thingParameterParameterSet

import com.askolds.homeinventory.featureParameter.data.repository.ThingParameterParameterSetRepository
import com.askolds.homeinventory.featureParameter.domain.model.ThingParameterSet
import com.askolds.homeinventory.featureParameter.domain.model.toThingParameterSet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetListGroupedBySetByThingId(
    private val repository: ThingParameterParameterSetRepository,
) {
    operator fun invoke(thingId: Int): Flow<List<ThingParameterSet>> {
        return repository.getListGroupedBySetByThingId(thingId).map { items ->
            items.map { item ->
                item.toThingParameterSet()
            }
        }
    }
}