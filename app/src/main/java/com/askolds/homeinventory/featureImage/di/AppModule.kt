package com.askolds.homeinventory.featureImage.di

import android.content.Context
import com.askolds.homeinventory.data.Database
import com.askolds.homeinventory.featureImage.data.repository.ImageRepository
import com.askolds.homeinventory.featureImage.data.repository.ImageRepositoryImpl
import com.askolds.homeinventory.featureImage.domain.usecase.Compress
import com.askolds.homeinventory.featureImage.domain.usecase.Get
import com.askolds.homeinventory.featureImage.domain.usecase.GetFileName
import com.askolds.homeinventory.featureImage.domain.usecase.ImageUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideImageRepository(@ApplicationContext context: Context, database: Database): ImageRepository = ImageRepositoryImpl(
        context, database.imageDao()
    )

    @Provides
    @Singleton
    fun provideImageUseCases(@ApplicationContext context: Context, repository: ImageRepository): ImageUseCases  = ImageUseCases(
        compress = Compress(context),
        get = Get(repository),
        getFileName = GetFileName(context)
    )
}