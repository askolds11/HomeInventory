package com.askolds.homeinventory.featureParameter.domain.usecase.parameterSetParameter

import com.askolds.homeinventory.featureParameter.data.repository.ParameterSetParameterRepository
import com.askolds.homeinventory.featureParameter.domain.model.ParameterListItem
import com.askolds.homeinventory.featureParameter.domain.model.toParameterListItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Gets only selected parameters for parameter set
 */
class GetFlowParameterList(
    private val repository: ParameterSetParameterRepository,
) {
    operator fun invoke(parameterSetId: Int): Flow<List<ParameterListItem>> {
        return repository.getFlowListBySetId(parameterSetId).map { parameters ->
            parameters.map {selectedItem ->
                selectedItem.toParameterListItem(selected = true)
            }
        }
    }
}