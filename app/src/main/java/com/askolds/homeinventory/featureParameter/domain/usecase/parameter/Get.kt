package com.askolds.homeinventory.featureParameter.domain.usecase.parameter

import com.askolds.homeinventory.featureParameter.data.repository.ParameterRepository
import com.askolds.homeinventory.featureParameter.domain.model.Parameter
import com.askolds.homeinventory.featureParameter.domain.model.toParameter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Get(
    private val repository: ParameterRepository,
) {
    suspend operator fun invoke(parameterId: Int): Parameter? {
        return withContext (Dispatchers.IO) {
            return@withContext repository.getById(parameterId)?.toParameter()
        }
    }
}