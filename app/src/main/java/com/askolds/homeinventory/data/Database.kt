package com.askolds.homeinventory.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.askolds.homeinventory.featureHome.data.dao.HomeDao
import com.askolds.homeinventory.featureHome.data.model.HomeEntity
import com.askolds.homeinventory.featureImage.data.dao.ImageDao
import com.askolds.homeinventory.featureImage.data.model.ImageEntity
import com.askolds.homeinventory.featureParameter.data.dao.ParameterDao
import com.askolds.homeinventory.featureParameter.data.model.ParameterEntity
import com.askolds.homeinventory.featureParameter.data.model.ParameterSetEntity
import com.askolds.homeinventory.featureParameter.data.model.ParameterSetParameterEntity
import com.askolds.homeinventory.featureThing.data.dao.ThingDao
import com.askolds.homeinventory.featureThing.data.model.ThingEntity

@Database(
    entities = [
        HomeEntity::class,
        ThingEntity::class,
        ImageEntity::class,
        ParameterEntity::class,
        ParameterSetEntity::class,
        ParameterSetParameterEntity::class,
    ],
    version = 1,
    exportSchema = true
)
abstract class Database: RoomDatabase() {
    abstract fun homeDao(): HomeDao
    abstract fun thingDao(): ThingDao
    abstract fun imageDao(): ImageDao
    abstract fun parameterDao(): ParameterDao

    companion object {
        const val DATABASE_NAME = "home_inventory_db"
    }
}