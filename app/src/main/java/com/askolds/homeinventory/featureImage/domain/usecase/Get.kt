package com.askolds.homeinventory.featureImage.domain.usecase

import com.askolds.homeinventory.featureImage.data.repository.ImageRepository
import com.askolds.homeinventory.featureImage.domain.model.Image
import com.askolds.homeinventory.featureImage.domain.model.toImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Get(
    private val repository: ImageRepository
) {
    suspend operator fun invoke(imageId: Int): Image? {
        return withContext (Dispatchers.IO) {
            return@withContext repository.getById(imageId)?.toImage()
        }
    }
}