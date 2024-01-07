package com.askolds.homeinventory.core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.askolds.homeinventory.featureHome.data.dao.HomeDao
import com.askolds.homeinventory.featureHome.data.model.HomeEntity
import com.askolds.homeinventory.featureImage.data.dao.ImageDao
import com.askolds.homeinventory.featureImage.data.model.ImageEntity
import com.askolds.homeinventory.featureImageNavigation.data.dao.ImageThingNavigationDao
import com.askolds.homeinventory.featureImageNavigation.data.model.ImageThingNavigationEntity
import com.askolds.homeinventory.featureParameter.data.dao.ParameterDao
import com.askolds.homeinventory.featureParameter.data.dao.ParameterSetDao
import com.askolds.homeinventory.featureParameter.data.dao.ParameterSetParameterDao
import com.askolds.homeinventory.featureParameter.data.dao.ThingParameterParameterSetDao
import com.askolds.homeinventory.featureParameter.data.dao.ThingParameterSetDao
import com.askolds.homeinventory.featureParameter.data.model.ParameterEntity
import com.askolds.homeinventory.featureParameter.data.model.ParameterSetEntity
import com.askolds.homeinventory.featureParameter.data.model.ParameterSetParameterEntity
import com.askolds.homeinventory.featureParameter.data.model.ThingParameterParameterSetEntity
import com.askolds.homeinventory.featureParameter.data.model.ThingParameterSetEntity
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
        ThingParameterParameterSetEntity::class,
        ThingParameterSetEntity::class,
        ImageThingNavigationEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class Database : RoomDatabase() {
    abstract fun homeDao(): HomeDao
    abstract fun thingDao(): ThingDao
    abstract fun imageDao(): ImageDao
    abstract fun parameterDao(): ParameterDao
    abstract fun parameterSetDao(): ParameterSetDao
    abstract fun parameterSetParameterDao(): ParameterSetParameterDao
    abstract fun thingParameterParameterSetDao(): ThingParameterParameterSetDao
    abstract fun thingParameterSetDao(): ThingParameterSetDao
    abstract fun imageThingNavigationDao(): ImageThingNavigationDao

    companion object {
        const val DATABASE_NAME = "home_inventory_db"
    }
}