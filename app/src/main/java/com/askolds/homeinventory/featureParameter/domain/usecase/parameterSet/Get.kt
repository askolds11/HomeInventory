package com.askolds.homeinventory.featureParameter.domain.usecase.parameterSet

import com.askolds.homeinventory.featureParameter.data.repository.ParameterSetRepository
import com.askolds.homeinventory.featureParameter.domain.model.ParameterSet
import com.askolds.homeinventory.featureParameter.domain.model.toParameterSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Get(
    private val repository: ParameterSetRepository,
) {
    suspend operator fun invoke(parameterSetId: Int): ParameterSet? {
        return withContext (Dispatchers.IO) {
            return@withContext repository.getById(parameterSetId)?.toParameterSet()
        }
    }
}