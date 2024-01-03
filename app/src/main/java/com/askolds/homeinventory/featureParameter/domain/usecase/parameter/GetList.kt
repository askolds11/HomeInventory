package com.askolds.homeinventory.featureParameter.domain.usecase.parameter

import com.askolds.homeinventory.featureParameter.data.repository.ParameterRepository
import com.askolds.homeinventory.featureParameter.domain.model.ParameterListItem
import com.askolds.homeinventory.featureParameter.domain.model.toParameterListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetList(
    private val repository: ParameterRepository,
) {
    suspend operator fun invoke(): List<ParameterListItem> {
        return withContext(Dispatchers.IO) {
            return@withContext repository.getList().map { item ->
                item.toParameterListItem()
            }
        }
    }
}