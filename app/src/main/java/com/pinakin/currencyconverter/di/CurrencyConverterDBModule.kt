package com.pinakin.currencyconverter.di

import android.content.Context
import androidx.room.Room
import com.pinakin.currencyconverter.data.local.CurrencyConverterDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CurrencyConverterDBModule {

    @Provides
    @Singleton
    fun providesCurrencyConverterDB(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        CurrencyConverterDB::class.java,
        "currency_converter_db"
    ).build()
}