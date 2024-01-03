package com.askolds.homeinventory.featureParameter.domain.usecase.parameter

import com.askolds.homeinventory.featureParameter.data.repository.ParameterRepository
import com.askolds.homeinventory.featureParameter.domain.model.Parameter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Update(
    private val repository: ParameterRepository,
) {
    suspend operator fun invoke(parameter: Parameter) {
        withContext(Dispatchers.IO) {
            repository.update(parameter.toEntity())
        }
    }
}