package com.askolds.homeinventory.featureHome.domain.usecase

import com.askolds.homeinventory.featureHome.data.repository.HomeRepository
import com.askolds.homeinventory.featureImage.data.repository.ImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeleteList (
    private val repository: HomeRepository,
    private val imageRepository: ImageRepository
) {
    suspend operator fun invoke(ids: List<Int>) {
        withContext(Dispatchers.IO) {
            val imageIds = repository.getImageIdsByIds(ids)
            imageIds.filterNotNull().forEach { id ->
                imageRepository.deleteById(id)
            }
            repository.deleteByIds(ids)
        }
    }
}