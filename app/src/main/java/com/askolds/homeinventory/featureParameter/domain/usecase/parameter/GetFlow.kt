package com.askolds.homeinventory.featureParameter.domain.usecase.parameter

import com.askolds.homeinventory.featureParameter.data.repository.ParameterRepository
import com.askolds.homeinventory.featureParameter.domain.model.Parameter
import com.askolds.homeinventory.featureParameter.domain.model.toParameter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetFlow(
    private val repository: ParameterRepository,
) {
    operator fun invoke(parameterId: Int): Flow<Parameter> {
        return repository.getFlowById(parameterId).map { item ->
            item.toParameter()
        }
    }
}