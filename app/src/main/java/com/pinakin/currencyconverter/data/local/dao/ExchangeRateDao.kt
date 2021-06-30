package com.pinakin.currencyconverter.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pinakin.currencyconverter.data.local.entity.ExchangeRateEntity
import com.pinakin.currencyconverter.models.ExchangeRate
import kotlinx.coroutines.flow.Flow

@Dao
interface ExchangeRateDao {

    @Query("SELECT * FROM exchange_rate")
    fun getExchangeRates(): Flow<List<ExchangeRateEntity>>

    @Query("SELECT * FROM exchange_rate WHERE code=:code")
    suspend fun getExchangeRateByCode(code: String): ExchangeRate

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exchangeRateEntity: ExchangeRateEntity)
}