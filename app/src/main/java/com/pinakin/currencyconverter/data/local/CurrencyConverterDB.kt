package com.pinakin.currencyconverter.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pinakin.currencyconverter.data.local.dao.CurrencyDao
import com.pinakin.currencyconverter.data.local.dao.ExchangeRateDao
import com.pinakin.currencyconverter.data.local.entity.CurrencyEntity
import com.pinakin.currencyconverter.data.local.entity.ExchangeRateEntity

@Database(
    entities = [CurrencyEntity::class, ExchangeRateEntity::class],
    version = 1
)
abstract class CurrencyConverterDB : RoomDatabase() {

    abstract fun getCurrencyDao(): CurrencyDao

    abstract fun getExchangeRateDao(): ExchangeRateDao
}