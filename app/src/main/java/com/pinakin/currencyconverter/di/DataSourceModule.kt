package com.pinakin.currencyconverter.di

import com.pinakin.currencyconverter.data.local.CurrencyConverterDB
import com.pinakin.currencyconverter.data.remote.CurrencyConverterApi
import com.pinakin.currencyconverter.datasource.LocalDataSource
import com.pinakin.currencyconverter.datasource.NetworkDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataSourceModule {

    @Provides
    @Singleton
    fun providesNetworkDataSource(api: CurrencyConverterApi) = NetworkDataSource(api)

    @Provides
    @Singleton
    fun providesLocalDataSource(db: CurrencyConverterDB) = LocalDataSource(db)
}