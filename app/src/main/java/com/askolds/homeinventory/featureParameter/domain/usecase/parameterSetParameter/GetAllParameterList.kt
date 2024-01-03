package com.askolds.homeinventory.featureParameter.domain.usecase.parameterSetParameter

import com.askolds.homeinventory.featureParameter.data.repository.ParameterSetParameterRepository
import com.askolds.homeinventory.featureParameter.domain.model.ParameterListItem
import com.askolds.homeinventory.featureParameter.domain.model.toParameterListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

/**
 * Gets selected and unselected parameters for parameter set
 */
class GetAllParameterList(
    private val repository: ParameterSetParameterRepository,
) {
    suspend operator fun invoke(parameterSetId: Int): List<ParameterListItem> {
        return withContext(Dispatchers.IO) {
            // get for specific parameter set
            val selectedParameters = repository.getFlowListBySetId(parameterSetId).first()
            val unselectedParameters = repository.getNotListBySetId(parameterSetId)
            return@withContext selectedParameters.map { selectedItem ->
                selectedItem.toParameterListItem(selected = true)
            } + unselectedParameters.map { unselectedItem ->
                unselectedItem.toParameterListItem(selected = false)
            }
        }
    }
}