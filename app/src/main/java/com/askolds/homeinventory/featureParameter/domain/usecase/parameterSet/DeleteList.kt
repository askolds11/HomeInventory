package com.askolds.homeinventory.featureParameter.domain.usecase.parameterSet

import com.askolds.homeinventory.featureParameter.data.repository.ParameterSetRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeleteList (
    private val repository: ParameterSetRepository,
) {
    suspend operator fun invoke(ids: List<Int>) {
        withContext(Dispatchers.IO) {
            repository.deleteByIds(ids)
        }
    }
}