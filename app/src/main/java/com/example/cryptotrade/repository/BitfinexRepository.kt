package com.example.cryptotrade.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cryptotrade.api.BitfinexApi
import com.example.cryptotrade.api.BitfinexApiService
import kotlinx.coroutines.withTimeout

class BitfinexRepository {

    private val bitfinexApiService: BitfinexApiService = BitfinexApi.createApi()
    private val _ticker: MutableLiveData<String> = MutableLiveData()
    private val _history: MutableLiveData<String> = MutableLiveData()

    val ticker: LiveData<String>
        get() = _ticker

    val history: LiveData<String>
        get() = _history

    suspend fun getTicker(tradingPair: String) {
        try {
            val result = withTimeout(5_000) {
                bitfinexApiService.getTicker(tradingPair)
            }

            _ticker.value = result.toString()
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

            _ticker.value = result
        } catch (error: Throwable) {
            throw BitfinexApiError("Unable to get ticker", error)
        }
    }

    class BitfinexApiError(message: String, cause: Throwable) : Throwable(message, cause)
}