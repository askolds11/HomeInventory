package com.askolds.homeinventory.featureParameter.domain.usecase.parameterSet

import com.askolds.homeinventory.featureParameter.data.model.ParameterSetParameterEntity
import com.askolds.homeinventory.featureParameter.data.repository.ParameterSetParameterRepository
import com.askolds.homeinventory.featureParameter.data.repository.ParameterSetRepository
import com.askolds.homeinventory.featureParameter.domain.model.ParameterSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Add (
    private val repository: ParameterSetRepository,
    private val parameterSetParameterRepository: ParameterSetParameterRepository,
) {
    suspend operator fun invoke(parameterSet: ParameterSet, parameterIds: List<Int>): Int {
        return withContext(Dispatchers.IO) {
            val id = repository.insert(parameterSet.toEntity())

            parameterSetParameterRepository.insertAll(
                parameterIds.map {parameterId ->
                    ParameterSetParameterEntity(
                        id = 0,
                        parameterId = parameterId,
                        parameterSetId = id
                    )
                }
            )

            return@withContext id
        }
    }
}