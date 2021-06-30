package com.pinakin.currencyconverter.repository

import com.pinakin.currencyconverter.datasource.LocalDataSource
import com.pinakin.currencyconverter.models.ExchangeRate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CurrencyConverterRepository @Inject constructor(
    private val localDataSource: LocalDataSource
) {

    val currencies: Flow<List<String>> = localDataSource.getCurrencies()

    val exchangeRates: Flow<List<ExchangeRate>> = localDataSource.getExchangeRates()
        .map { rates ->
            rates.map { rate -> ExchangeRate(rate.code, rate.rate) }
        }


    suspend fun convert(amount: String, fromCurrency: String, toCurrency: String): Flow<Double> =
        flow {
            val sourceExchangeRate = localDataSource.getExchangeRateByCode(fromCurrency)
            val destinationExchangeRate = localDataSource.getExchangeRateByCode(toCurrency)
            val postConversation =
                ((amount.toDouble() / sourceExchangeRate.rate) * destinationExchangeRate.rate)
            emit(postConversation)
        }
}