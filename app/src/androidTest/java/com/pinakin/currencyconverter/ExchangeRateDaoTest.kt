package com.pinakin.currencyconverter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.pinakin.currencyconverter.data.local.CurrencyConverterDB
import com.pinakin.currencyconverter.data.local.dao.ExchangeRateDao
import com.pinakin.currencyconverter.data.local.entity.ExchangeRateEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExchangeRateDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var database: CurrencyConverterDB

    private lateinit var exchangeRateDao: ExchangeRateDao

    @Before
    fun setUp() {

        val context = InstrumentationRegistry.getInstrumentation().context

        database = Room.inMemoryDatabaseBuilder(
            context,
            CurrencyConverterDB::class.java
        ).allowMainThreadQueries()
            .build()
        exchangeRateDao = database.getExchangeRateDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun givenExchangeRateInINR_DB_ShouldReturn_INR() = runBlocking {

        val exchangeRate = ExchangeRateEntity("INR", 74.24)
        exchangeRateDao.insert(exchangeRate)
        val fromDb = exchangeRateDao.getExchangeRates().first()[0]
        assertEquals(exchangeRate.code, fromDb.code)
        assertEquals(exchangeRate.rate, fromDb.rate, 0.0)
    }
}