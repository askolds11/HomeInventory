package com.askolds.homeinventory.featureHome.domain.usecase

import com.askolds.homeinventory.featureHome.data.repository.HomeRepository
import com.askolds.homeinventory.featureHome.domain.HomeConstants
import com.askolds.homeinventory.featureHome.domain.model.Home
import com.askolds.homeinventory.featureImage.data.repository.ImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Update (
    private val repository: HomeRepository,
    private val imageRepository: ImageRepository
) {
    suspend operator fun invoke(home: Home) {
        withContext(Dispatchers.IO) {
            var homeWithImage = home
            val oldHome = repository.getById(home.id)
            // image changed
            if (oldHome?.imageId != home.image?.id) {
                // old image exists, delete
                if (oldHome?.imageId != null)
                    imageRepository.deleteById(oldHome.imageId)
                // new image exists, insert
                if (home.image != null) {
                    val imageId = imageRepository.insert(
                        home.image.toEntity(),
                        HomeConstants.homeImageSubfolder
                    )
                    homeWithImage = home.copy(
                        image = home.image.copy(
                            id = imageId
                        )
                    )
                }
            }
            repository.update(homeWithImage.toEntity())
        }
    }
}