package com.example.cryptotrade.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BitfinexApiService {

    @GET("ticker/{tradingPair}")
    suspend fun getTicker(@Path("tradingPair") tradingPair: String) : String

    @GET("trades/{tradingPair}/hist?limit=5")
    suspend fun getHistory(@Path("tradingPair") tradingPair: String,
                           @Query("start") start: String,
                           @Query("end") end: String) : String
}