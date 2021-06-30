package com.pinakin.currencyconverter.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.pinakin.currencyconverter.datasource.LocalDataSource
import com.pinakin.currencyconverter.datasource.NetworkDataSource
import com.pinakin.currencyconverter.utils.data
import com.pinakin.currencyconverter.utils.succeeded
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay

@HiltWorker
class ExchangeRateWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val networkDataSource: NetworkDataSource,
    private val localDataSource: LocalDataSource
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {

        val currencyListResponse = networkDataSource.fetchCurrencyList()
        if (currencyListResponse.succeeded) {
            currencyListResponse.data?.forEach { currency ->
                localDataSource.insertCurrency(currency)
            }
        }

        delay(5000)

        val exchangeRateListResponse = networkDataSource.fetchExchangeRateList()
        if (exchangeRateListResponse.succeeded) {
            exchangeRateListResponse.data?.forEach { exchangeRate ->
                localDataSource.insertExchangeRate(exchangeRate)
            }
        }

        return Result.success()
    }
}