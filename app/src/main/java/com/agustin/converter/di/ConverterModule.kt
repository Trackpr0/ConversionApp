package com.agustin.converter.di

import com.agustin.converter.data.ConversionRepository
import com.agustin.converter.data.DefaultConversionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ConverterModule {

    @Binds
    @Singleton
    abstract fun bindConversionRepository(
        impl: DefaultConversionRepository
    ): ConversionRepository
}
