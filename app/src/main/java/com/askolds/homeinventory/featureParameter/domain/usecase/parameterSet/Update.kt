package com.askolds.homeinventory.featureParameter.domain.usecase.parameterSet

import com.askolds.homeinventory.featureParameter.data.model.ParameterSetParameterEntity
import com.askolds.homeinventory.featureParameter.data.repository.ParameterSetParameterRepository
import com.askolds.homeinventory.featureParameter.data.repository.ParameterSetRepository
import com.askolds.homeinventory.featureParameter.domain.model.ParameterSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class Update (
    private val repository: ParameterSetRepository,
    private val parameterSetParameterRepository: ParameterSetParameterRepository
) {
    suspend operator fun invoke(parameterSet: ParameterSet, parameterIds: List<Int>) {
        withContext(Dispatchers.IO) {
            repository.update(parameterSet.toEntity())

            val oldParameterIds = parameterSetParameterRepository
                .getFlowListBySetId(parameterSet.id).first().map { item -> item.id}.toSet()

            val newParameterIds = parameterIds.toSet()
            val idsToDelete = oldParameterIds.subtract(newParameterIds)
            val parameterSetParameterList = newParameterIds.subtract(oldParameterIds)
                .map {parameterId ->
                    ParameterSetParameterEntity(
                        id = 0,
                        parameterId = parameterId,
                        parameterSetId = parameterSet.id
                    )
                }

            parameterSetParameterRepository.deleteByParameterIds(idsToDelete)
            parameterSetParameterRepository.insertAll(parameterSetParameterList)
        }
    }
}