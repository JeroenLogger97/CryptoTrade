package com.example.cryptotrade.api

import com.example.cryptotrade.model.HistoryResponse
import com.example.cryptotrade.model.MultipleTickersResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BitfinexApiService {

    @GET("tickers")
    suspend fun getMultipleTickers(@Query("symbols", encoded = true) symbols: String) : MultipleTickersResponse

    @GET("trades/{tradingPair}/hist?limit=5")
    suspend fun getHistory(@Path("tradingPair") tradingPair: String,
                           @Query("start") start: String,
                           @Query("end") end: String) : HistoryResponse
}