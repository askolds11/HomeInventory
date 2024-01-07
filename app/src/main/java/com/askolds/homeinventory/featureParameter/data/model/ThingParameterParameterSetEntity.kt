package com.askolds.homeinventory.featureParameter.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.askolds.homeinventory.featureThing.data.model.ThingEntity

@Entity(
    tableName = "thing_parameter_parameter_set",
    foreignKeys = [
        ForeignKey(
            entity = ThingEntity::class,
            parentColumns = ["id"],
            childColumns = ["thingId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ParameterEntity::class,
            parentColumns = ["id"],
            childColumns = ["parameterId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ParameterSetParameterEntity::class,
            parentColumns = ["id"],
            childColumns = ["parameterSetParameterId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ThingParameterSetEntity::class,
            parentColumns = ["id"],
            childColumns = ["thingParameterSetId"],
            onDelete = ForeignKey.CASCADE
        ),
    ],
    indices = [
        Index("thingId"),
        Index("parameterId"),
        Index("parameterSetParameterId"),
        Index("thingParameterSetId")
    ]
)
data class ThingParameterParameterSetEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val value: String?,
    val thingId: Int,
    val parameterId: Int,
    val parameterSetParameterId: Int?,
    val thingParameterSetId: Int?,
)