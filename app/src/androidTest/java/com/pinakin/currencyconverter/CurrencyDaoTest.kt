package com.pinakin.currencyconverter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.pinakin.currencyconverter.data.local.CurrencyConverterDB
import com.pinakin.currencyconverter.data.local.dao.CurrencyDao
import com.pinakin.currencyconverter.data.local.entity.CurrencyEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CurrencyDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var database: CurrencyConverterDB

    private lateinit var currencyDao: CurrencyDao

    @Before
    fun setUp() {

        val context = InstrumentationRegistry.getInstrumentation().context

        database = Room.inMemoryDatabaseBuilder(
            context,
            CurrencyConverterDB::class.java
        ).allowMainThreadQueries()
            .build()
        currencyDao = database.getCurrencyDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertTest() = runBlocking {
        val currencyEntity = CurrencyEntity("TEST", "TEST NAME")
        currencyDao.insert(currencyEntity)
        val currency = currencyDao.getCurrencies().first()[0]
        assertEquals(currencyEntity.code, currency)
    }
}