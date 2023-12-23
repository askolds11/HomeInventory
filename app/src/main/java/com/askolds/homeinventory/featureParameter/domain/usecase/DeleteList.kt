package com.askolds.homeinventory.featureParameter.domain.usecase

import com.askolds.homeinventory.featureHome.data.repository.HomeRepository
import com.askolds.homeinventory.featureImage.data.repository.ImageRepository
import com.askolds.homeinventory.featureParameter.data.repository.ParameterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeleteList (
    private val repository: ParameterRepository,
) {
    suspend operator fun invoke(ids: List<Int>) {
        withContext(Dispatchers.IO) {
            repository.deleteByIds(ids)
        }
    }
}