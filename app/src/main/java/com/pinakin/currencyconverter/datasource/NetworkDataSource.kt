package com.pinakin.currencyconverter.datasource

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.pinakin.currencyconverter.data.remote.CurrencyConverterApi
import com.pinakin.currencyconverter.models.Currency
import com.pinakin.currencyconverter.models.ExchangeRate
import com.pinakin.currencyconverter.utils.Result
import javax.inject.Inject

class NetworkDataSource @Inject constructor(
    private val api: CurrencyConverterApi
) {

    suspend fun fetchCurrencyList(): Result<List<Currency>> {
        return try {
            val result = api.getCurrencies()
            if (result.isSuccessful) {
                val json = processJsonResponse(result.body()?.string())
                val currencyObject = json.getAsJsonObject("currencies")
                val currencies = currencyObject.entrySet()
                    .map { Currency(it.component1(), it.component2().asString) }
                Result.Success(currencies)
            } else {
                Result.Error(Exception(result.errorBody()?.string()))
            }
        } catch (e: Exception) {
            Result.Error(Exception("Unknown Error"))
        }
    }

    suspend fun fetchExchangeRateList(): Result<List<ExchangeRate>> {
        return try {
            val result = api.getExchangeRate()
            if (result.isSuccessful) {
                val json = processJsonResponse(result.body()?.string())
                val exchangeRateObject = json.getAsJsonObject("quotes")
                val exchangeRates = exchangeRateObject.entrySet().map {
                    ExchangeRate(it.component1(), it.component2().asDouble)
                }
                Result.Success(exchangeRates)
            } else {
                Result.Error(Exception(result.errorBody()?.string()))
            }
        } catch (e: Exception) {
            Result.Error(Exception("Unknown Error"))
        }
    }

    private fun processJsonResponse(response: String?): JsonObject {
        val json = Gson().fromJson(response, JsonObject::class.java)
        val status = json.get("success").asBoolean
        if (status) {
            return json
        } else {
            throw Exception("Error from server")
        }
    }
}