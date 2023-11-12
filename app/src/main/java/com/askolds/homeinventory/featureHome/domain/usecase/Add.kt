package com.askolds.homeinventory.featureHome.domain.usecase

import com.askolds.homeinventory.featureHome.domain.model.Home
import com.askolds.homeinventory.featureHome.data.repository.HomeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Add (
    private val repository: HomeRepository
) {
    suspend operator fun invoke(home: Home) {
        withContext(Dispatchers.IO) {
            repository.insert(home.toEntity())
        }
    }

    suspend operator fun invoke(name: String) {
        val home = Home(name = name)
        invoke(home)
    }
}