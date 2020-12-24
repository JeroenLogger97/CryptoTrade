package com.example.cryptotrade.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cryptotrade.api.BitfinexApi
import com.example.cryptotrade.api.BitfinexApiService
import com.example.cryptotrade.model.HistoryResponse
import com.example.cryptotrade.model.TickerResponse
import kotlinx.coroutines.withTimeout

class BitfinexRepository {

    private val bitfinexApiService: BitfinexApiService = BitfinexApi.createApi()
    private val _ticker: MutableLiveData<TickerResponse> = MutableLiveData()
    private val _history: MutableLiveData<HistoryResponse> = MutableLiveData()

    val ticker: LiveData<TickerResponse>
        get() = _ticker

    val history: LiveData<HistoryResponse>
        get() = _history

    suspend fun getTicker(tradingPair: String) {
        try {
            val result = withTimeout(5_000) {
                bitfinexApiService.getTicker(tradingPair)
            }

            _ticker.value = result
        } catch (error: Throwable) {
            throw BitfinexApiError("Unable to get ticker", error)
        }
    }

    suspend fun getHistory(tradingPair: String,
                           startInMillis: String,
                           endInMillis: String) {
        try {
            val result = withTimeout(5_000) {
                bitfinexApiService.getHistory(tradingPair, startInMillis, endInMillis)
            }

            _history.value = result
        } catch (error: Throwable) {
            throw BitfinexApiError("Unable to get history", error)
        }
    }

    class BitfinexApiError(message: String, cause: Throwable) : Throwable(message, cause)
}