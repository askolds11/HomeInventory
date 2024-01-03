package com.askolds.homeinventory.featureParameter.domain.usecase.parameterSet

import com.askolds.homeinventory.featureParameter.data.repository.ParameterSetRepository
import com.askolds.homeinventory.featureParameter.domain.model.ParameterSetListItem
import com.askolds.homeinventory.featureParameter.domain.model.toParameterSetListItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetListFlow(
    private val repository: ParameterSetRepository,
) {
    operator fun invoke(): Flow<List<ParameterSetListItem>> {
        return repository.getList().map { items ->
            items.map { item ->
                item.toParameterSetListItem()
            }
        }
    }
}