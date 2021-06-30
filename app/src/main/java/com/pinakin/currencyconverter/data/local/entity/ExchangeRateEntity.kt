package com.pinakin.currencyconverter.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exchange_rate")
data class ExchangeRateEntity(
    @PrimaryKey
    val code: String,
    val rate: Double
)
