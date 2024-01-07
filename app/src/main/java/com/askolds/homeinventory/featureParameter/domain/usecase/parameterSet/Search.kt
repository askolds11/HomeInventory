package com.askolds.homeinventory.featureParameter.domain.usecase.parameterSet

import com.askolds.homeinventory.core.domain.toSearchable
import com.askolds.homeinventory.featureParameter.data.repository.ParameterSetRepository
import com.askolds.homeinventory.featureParameter.domain.model.ParameterSetListItem
import com.askolds.homeinventory.featureParameter.domain.model.toParameterSetListItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class Search(
    private val repository: ParameterSetRepository,
) {
    operator fun invoke(name: String): Flow<List<ParameterSetListItem>> {
        return repository.search(name.toSearchable()).map { items ->
            items.map { item ->
                item.toParameterSetListItem()
            }
        }
    }
}