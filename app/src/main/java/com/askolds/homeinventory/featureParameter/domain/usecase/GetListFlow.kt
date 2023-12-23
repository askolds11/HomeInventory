package com.askolds.homeinventory.featureParameter.domain.usecase

import com.askolds.homeinventory.featureParameter.data.repository.ParameterRepository
import com.askolds.homeinventory.featureParameter.domain.model.ParameterListItem
import com.askolds.homeinventory.featureParameter.domain.model.toParameterListItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetListFlow(
    private val repository: ParameterRepository,
) {
    operator fun invoke(): Flow<List<ParameterListItem>> {
        return repository.getList().map { items ->
            items.map { item ->
                item.toParameterListItem()
            }
        }
    }
}