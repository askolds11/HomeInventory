package com.askolds.homeinventory.featureParameter.domain.usecase.parameter

import com.askolds.homeinventory.domain.toSearchable
import com.askolds.homeinventory.featureParameter.data.repository.ParameterRepository
import com.askolds.homeinventory.featureParameter.domain.model.ParameterListItem
import com.askolds.homeinventory.featureParameter.domain.model.toParameterListItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class Search(
    private val repository: ParameterRepository,
) {
    operator fun invoke(name: String): Flow<List<ParameterListItem>> {
        return repository.getFlowListByName(name.toSearchable()).map { items ->
            items.map { item ->
                item.toParameterListItem()
            }
        }
    }
}