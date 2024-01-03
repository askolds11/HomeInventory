package com.askolds.homeinventory.featureParameter.data.repository

import com.askolds.homeinventory.featureParameter.data.model.ParameterSetEntity
import kotlinx.coroutines.flow.Flow

interface ParameterSetRepository {
    suspend fun insert(parameterSet: ParameterSetEntity): Int
    suspend fun update(home: ParameterSetEntity)
    suspend fun getById(id: Int): ParameterSetEntity?
    fun getFlowById(id: Int): Flow<ParameterSetEntity>
    fun getList(): Flow<List<ParameterSetEntity>>
    fun search(name: String): Flow<List<ParameterSetEntity>>
    suspend fun nameExists(name: String, excludeName: String?): Boolean
    suspend fun deleteByIds(ids: List<Int>)
}