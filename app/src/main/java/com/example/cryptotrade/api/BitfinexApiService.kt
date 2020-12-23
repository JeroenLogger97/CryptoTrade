package com.example.cryptotrade.api

import retrofit2.http.GET
import retrofit2.http.Path

interface BitfinexApiService {

    @GET("ticker/{tradingPair}")
    suspend fun getTicker(@Path("tradingPair") tradingPair: String) : String
}