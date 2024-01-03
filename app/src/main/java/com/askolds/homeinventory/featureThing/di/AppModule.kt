package com.askolds.homeinventory.featureThing.di

import com.askolds.homeinventory.data.Database
import com.askolds.homeinventory.featureHome.domain.usecase.*
import com.askolds.homeinventory.featureImage.data.repository.ImageRepository
import com.askolds.homeinventory.featureThing.data.repository.ThingRepository
import com.askolds.homeinventory.featureThing.data.repository.ThingRepositoryImpl
import com.askolds.homeinventory.featureThing.domain.usecase.*
import com.askolds.homeinventory.featureThing.domain.usecase.thing.Add
import com.askolds.homeinventory.featureThing.domain.usecase.thing.DeleteList
import com.askolds.homeinventory.featureThing.domain.usecase.thing.Get
import com.askolds.homeinventory.featureThing.domain.usecase.thing.GetFlow
import com.askolds.homeinventory.featureThing.domain.usecase.thing.GetListFlow
import com.askolds.homeinventory.featureThing.domain.usecase.thing.Search
import com.askolds.homeinventory.featureThing.domain.usecase.thing.ThingUseCases
import com.askolds.homeinventory.featureThing.domain.usecase.thing.Update
import com.askolds.homeinventory.featureThing.domain.usecase.thing.validation.ValidateName
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideThingRepository(database: Database): ThingRepository = ThingRepositoryImpl(
        database.thingDao()
    )

    @Provides
    @Singleton
    fun provideThingUseCases(repository: ThingRepository, imageRepository: ImageRepository): ThingUseCases = ThingUseCases(
        get = Get(repository, imageRepository),
        getFlow = GetFlow(repository, imageRepository),
        getListFlow = GetListFlow(repository, imageRepository),
        add = Add(repository, imageRepository),
        update = Update(repository, imageRepository),
        search = Search(repository),
        deleteList = DeleteList(repository, imageRepository),
        validateName = ValidateName()
    )
}