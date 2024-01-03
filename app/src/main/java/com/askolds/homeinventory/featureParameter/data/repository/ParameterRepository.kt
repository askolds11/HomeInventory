package com.askolds.homeinventory.featureParameter.data.repository

import com.askolds.homeinventory.featureParameter.data.model.ParameterEntity
import kotlinx.coroutines.flow.Flow

interface ParameterRepository {
    suspend fun insert(parameter: ParameterEntity): Int
    suspend fun update(parameter: ParameterEntity)
    suspend fun getById(id: Int): ParameterEntity?
    fun getFlowById(id: Int): Flow<ParameterEntity>
    fun getFlowList(): Flow<List<ParameterEntity>>
    suspend fun getList(): List<ParameterEntity>
    fun getFlowListByName(name: String): Flow<List<ParameterEntity>>
    suspend fun nameExists(name: String, excludeName: String?): Boolean
    suspend fun deleteByIds(ids: List<Int>)
//    suspend fun getImageIdsByIds(ids: List<Int>): List<Int?>
}