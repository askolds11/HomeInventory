package com.askolds.homeinventory.featureHome.data.repository

import com.askolds.homeinventory.featureHome.data.model.HomeEntity
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getList(): Flow<List<HomeEntity>>
    suspend fun insert(home: HomeEntity)
    suspend fun getById(id: Int): HomeEntity?
    suspend fun nameExists(name: String, excludeName: String?): Boolean
    fun search(name: String): Flow<List<HomeEntity>>
}