package com.pinakin.currencyconverter.di

import com.pinakin.currencyconverter.datasource.LocalDataSource
import com.pinakin.currencyconverter.repository.CurrencyConverterRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideCurrencyRepository(localDataSource: LocalDataSource) =
        CurrencyConverterRepository(
            localDataSource
        )
}