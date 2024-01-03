package com.askolds.homeinventory.featureParameter.domain.usecase.parameterSet

import com.askolds.homeinventory.featureParameter.data.repository.ParameterSetRepository
import com.askolds.homeinventory.featureParameter.domain.model.ParameterSet
import com.askolds.homeinventory.featureParameter.domain.model.toParameterSet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetFlow(
    private val repository: ParameterSetRepository,
) {
    operator fun invoke(parameterSetId: Int): Flow<ParameterSet> {
        return repository.getFlowById(parameterSetId).map { item ->
            item.toParameterSet()
        }
    }
}