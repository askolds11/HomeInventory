package com.askolds.homeinventory.featureThing.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.askolds.homeinventory.featureThing.data.model.ThingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ThingDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(item: ThingEntity): Long

    @Update
    suspend fun update(thing: ThingEntity)

    @Query("""
        SELECT * FROM thing
        WHERE homeId = :homeId AND parentId IS :parentId
        ORDER BY LOWER(name), name""")
    fun getList(homeId: Int, parentId: Int?): Flow<List<ThingEntity>>

    @Query("""
        SELECT * FROM thing
        WHERE parentId = :parentId
        ORDER BY LOWER(name), name""")
    fun getList(parentId: Int?): Flow<List<ThingEntity>>

    @Query("SELECT * FROM thing WHERE id = :id")
    suspend fun getById(id: Int): ThingEntity?

    @Query("SELECT * FROM thing WHERE id = :id")
    fun getFlowById(id: Int): Flow<ThingEntity>

    @Query("""
        SELECT * FROM thing
        WHERE (:homeId IS NULL OR homeId = :homeId)
            AND (:parentId IS NULL OR parentId IS :parentId)
            AND searchName LIKE '%' || :name || '%'
        ORDER BY LOWER(name), name
        """)
    fun search(homeId: Int?, parentId: Int?, name: String): Flow<List<ThingEntity>>

    @Query("""
        DELETE FROM thing WHERE id IN (:ids)
    """)
    suspend fun deleteByIds(ids: List<Int>)
}