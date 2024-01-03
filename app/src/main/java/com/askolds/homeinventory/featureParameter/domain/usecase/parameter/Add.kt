package com.askolds.homeinventory.featureParameter.domain.usecase.parameter

import com.askolds.homeinventory.featureParameter.data.repository.ParameterRepository
import com.askolds.homeinventory.featureParameter.domain.model.Parameter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Add (
    private val repository: ParameterRepository,
) {
    suspend operator fun invoke(parameter: Parameter): Int {
        return withContext(Dispatchers.IO) {
            return@withContext repository.insert(parameter.toEntity())
        }
    }
}