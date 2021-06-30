package com.pinakin.currencyconverter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.pinakin.currencyconverter.data.local.CurrencyConverterDB
import com.pinakin.currencyconverter.data.local.entity.ExchangeRateEntity
import com.pinakin.currencyconverter.datasource.LocalDataSource
import com.pinakin.currencyconverter.repository.CurrencyConverterRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CurrencyConverterRepositoryTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var database: CurrencyConverterDB

    lateinit var localDataSource: LocalDataSource

    lateinit var repository: CurrencyConverterRepository

    @Before
    fun setUp(){
        val context = InstrumentationRegistry.getInstrumentation().context

        database = Room.inMemoryDatabaseBuilder(
            context,
            CurrencyConverterDB::class.java
        ).allowMainThreadQueries()
            .build()

        localDataSource = LocalDataSource(database)

        repository = CurrencyConverterRepository(localDataSource)
    }

    @After
    fun tearDown(){
        database.close()
    }

    @Test
    fun given_INT_To_JPY() = runBlocking {
        val inrRate = ExchangeRateEntity("INR",74.24)
        val jpyRate = ExchangeRateEntity("JPY",111.05)

        database.getExchangeRateDao().insert(inrRate)
        database.getExchangeRateDao().insert(jpyRate)

        // 6685.28 = (10000/111.05)*74.24
        // formula
        // rate = (amount/sourceRate)*destinationRate
        val rate = repository.convert("10000","JPY","INR").first()
        val formattedRate = "%.2f".format(rate)
        assertEquals("6685.28",formattedRate)
    }
}