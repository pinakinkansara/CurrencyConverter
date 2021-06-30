package com.pinakin.currencyconverter.di

import com.google.gson.GsonBuilder
import com.pinakin.currencyconverter.data.remote.CurrencyConverterApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CurrencyConverterApiModule {

    @Provides
    fun provideLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    fun provideHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .hostnameVerifier { _, _ ->
                return@hostnameVerifier true
            }
            .build()

    @Provides
    @Singleton
    fun provideCurrencyConverterApi(client: OkHttpClient): CurrencyConverterApi = Retrofit.Builder()
        .baseUrl("http://api.currencylayer.com/")
        .client(client)
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder()
                    .setLenient()
                    .create()
            )
        )
        .build()
        .create(CurrencyConverterApi::class.java)

}