package com.example.cryptotrade.api

import com.example.cryptotrade.api.converter.CustomConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit

class BitfinexApi {
    companion object {
        private const val baseUrl = "https://api-pub.bitfinex.com/v2/"

        fun createApi(): BitfinexApiService {
            // Create an OkHttpClient to be able to make a log of the network traffic
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()

            // Create the Retrofit instance
            val triviaApi = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(converterFactory())
                .build()

            // Return the Retrofit NumbersApiService
            return triviaApi.create(BitfinexApiService::class.java)
        }

        private fun converterFactory() : Converter.Factory {
            return CustomConverterFactory.create()
        }
    }

}