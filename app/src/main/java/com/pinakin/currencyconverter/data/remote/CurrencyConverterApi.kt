package com.pinakin.currencyconverter.data.remote

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET

interface CurrencyConverterApi {

    @GET("list?access_key=f94bdb7aba8332d91db01ff7a6906256")
    suspend fun getCurrencies(): Response<ResponseBody>

    @GET("live?access_key=f94bdb7aba8332d91db01ff7a6906256")
    suspend fun getExchangeRate(): Response<ResponseBody>
}