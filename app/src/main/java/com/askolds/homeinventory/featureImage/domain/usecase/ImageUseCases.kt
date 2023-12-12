package com.askolds.homeinventory.featureImage.domain.usecase

data class ImageUseCases(
    val compress: Compress,
    val getFileName: GetFileName,
    val get: Get,
)
