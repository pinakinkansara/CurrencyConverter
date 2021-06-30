package com.pinakin.currencyconverter.repository

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.pinakin.currencyconverter.data.local.CurrencyConverterDB
import com.pinakin.currencyconverter.data.local.entity.CurrencyEntity
import com.pinakin.currencyconverter.data.local.entity.ExchangeRateEntity
import com.pinakin.currencyconverter.data.remote.CurrencyConverterApi
import com.pinakin.currencyconverter.models.ExchangeRate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CurrencyConverterRepository @Inject constructor(
    private val db: CurrencyConverterDB,
    private val api: CurrencyConverterApi
) {

    fun getCurrencies(): Flow<List<String>> = flow {

        db.getCurrencyDao().getCurrencies().collect { currencyEntities ->
            if (currencyEntities.isEmpty()) {
                getCurrencyListFromRemote()
            }
            emit(currencyEntities.map { it.code })
        }
    }

    private suspend fun getCurrencyListFromRemote() {
        val result = api.getCurrencies()
        if (result.isSuccessful) {
            val json = Gson().fromJson(result.body()?.string(), JsonObject::class.java)
            val currencyObject = json.getAsJsonObject("currencies")
            currencyObject.entrySet().forEach() {
                db.getCurrencyDao()
                    .insert(CurrencyEntity(it.component1(), it.component2().asString))
            }
        }
    }

    fun getExchangeRates(): Flow<List<ExchangeRate>> = flow {

        db.getExchangeRateDao().getExchangeRates().collect { exchangeRates ->
            if (exchangeRates.isEmpty()) {
                getExchangeRateFromRemote()
            }
            emit(exchangeRates.map { ExchangeRate(it.code, it.rate) })
        }
    }

    private suspend fun getExchangeRateFromRemote() {
        val result = api.getExchangeRate()
        if (result.isSuccessful) {
            val json = Gson().fromJson(result.body()?.string(), JsonObject::class.java)
            val exchangeRateObject = json.getAsJsonObject("quotes")
            exchangeRateObject.entrySet().forEach {
                db.getExchangeRateDao()
                    .insert(
                        ExchangeRateEntity(
                            it.component1().removePrefix("USD"),
                            it.component2().asDouble
                        )
                    )
            }
        }
    }

    suspend fun convert(amount: String, fromCurrency: String, toCurrency: String): Flow<Double> =
        flow {
            val sourceExchangeRate = db.getExchangeRateDao().getExchangeRateByCode(fromCurrency)
            val destinationExchangeRate = db.getExchangeRateDao().getExchangeRateByCode(toCurrency)
            val postConversation =
                ((amount.toDouble() / sourceExchangeRate.rate) * destinationExchangeRate.rate)
            emit(postConversation)
        }
}