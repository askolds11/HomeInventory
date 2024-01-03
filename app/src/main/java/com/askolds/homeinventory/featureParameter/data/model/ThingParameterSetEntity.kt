package com.askolds.homeinventory.featureParameter.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.askolds.homeinventory.featureThing.data.model.ThingEntity

@Entity(
    tableName = "thing_parameter_set",
    foreignKeys = [
        ForeignKey(
            entity = ThingEntity::class,
            parentColumns = ["id"],
            childColumns = ["thingId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ParameterSetEntity::class,
            parentColumns = ["id"],
            childColumns = ["parameterSetId"],
            onDelete = ForeignKey.CASCADE
        ),
    ],
    indices = [
        Index("thingId"),
        Index("parameterSetId")
    ]
)
data class ThingParameterSetEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val thingId: Int,
    val parameterSetId: Int,
)