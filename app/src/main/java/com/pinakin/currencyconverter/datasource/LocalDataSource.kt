package com.pinakin.currencyconverter.datasource

import com.pinakin.currencyconverter.data.local.CurrencyConverterDB
import com.pinakin.currencyconverter.data.local.entity.CurrencyEntity
import com.pinakin.currencyconverter.data.local.entity.ExchangeRateEntity
import com.pinakin.currencyconverter.models.Currency
import com.pinakin.currencyconverter.models.ExchangeRate
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val db: CurrencyConverterDB
) {

    suspend fun insertCurrency(currency: Currency) {
        val currencyEntity = CurrencyEntity(currency.code, currency.name)
        db.getCurrencyDao().insert(currencyEntity)
    }

    suspend fun insertExchangeRate(exchangeRate: ExchangeRate) {
        val exchangeRateEntity =
            ExchangeRateEntity(exchangeRate.code.removePrefix("USD"), exchangeRate.rate)
        db.getExchangeRateDao().insert(exchangeRateEntity)
    }

    fun getCurrencies() = db.getCurrencyDao().getCurrencies()

    fun getExchangeRates() = db.getExchangeRateDao().getExchangeRates()

    suspend fun getExchangeRateByCode(code: String) = db.getExchangeRateDao().getExchangeRateByCode(code)

}