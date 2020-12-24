package com.example.cryptotrade.api

import com.example.cryptotrade.model.TickerResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BitfinexApiService {

    @GET("ticker/{tradingPair}")
    suspend fun getTicker(@Path("tradingPair") tradingPair: String) : TickerResponse

    @GET("trades/{tradingPair}/hist?limit=5")
    suspend fun getHistory(@Path("tradingPair") tradingPair: String,
                           @Query("start") start: String,
                           @Query("end") end: String) : String
}