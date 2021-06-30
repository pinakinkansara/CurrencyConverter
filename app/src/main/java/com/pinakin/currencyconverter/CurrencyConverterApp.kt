package com.pinakin.currencyconverter

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerFactory
import com.pinakin.currencyconverter.worker.ExchangeRateWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class CurrencyConverterApp : Application(), Configuration.Provider {

    @Inject lateinit var workerFactory: HiltWorkerFactory


    override fun onCreate() {
        super.onCreate()

        val workRequest = PeriodicWorkRequestBuilder<ExchangeRateWorker>(30,TimeUnit.MINUTES).build()
        WorkManager.getInstance(this).enqueue(workRequest)
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }
}