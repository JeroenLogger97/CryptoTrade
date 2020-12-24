package com.example.cryptotrade.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cryptotrade.api.BitfinexApi
import com.example.cryptotrade.api.BitfinexApiService
import com.example.cryptotrade.model.HistoryResponse
import com.example.cryptotrade.model.MultipleTickersResponse
import kotlinx.coroutines.withTimeout

class BitfinexRepository {

    private val bitfinexApiService: BitfinexApiService = BitfinexApi.createApi()
    private val _tickers: MutableLiveData<MultipleTickersResponse> = MutableLiveData()
    private val _history: MutableLiveData<HistoryResponse> = MutableLiveData()

    val tickers: LiveData<MultipleTickersResponse>
        get() = _tickers

    val history: LiveData<HistoryResponse>
        get() = _history

    suspend fun getMultipleTickers(vararg tradingPairs: String) {
        try {
            var symbols = ""
            for (tradingPair in tradingPairs) {
                symbols += addPrefixToTradingPair(tradingPair)
                symbols += ","
            }
            symbols = symbols.substring(0, symbols.length - 1)

            val result = withTimeout(5_000) {
                bitfinexApiService.getMultipleTickers(symbols)
            }

            _tickers.value = result
        } catch (error: Throwable) {
            throw BitfinexApiError("Unable to get multiple tickers", error)
        }
    }

    suspend fun getHistory(tradingPair: String,
                           startInMillis: String,
                           endInMillis: String) {
        try {
            val result = withTimeout(5_000) {
                bitfinexApiService.getHistory(addPrefixToTradingPair(tradingPair), startInMillis, endInMillis)
            }

            _history.value = result
        } catch (error: Throwable) {
            throw BitfinexApiError("Unable to get history", error)
        }
    }

    // add a 't' before trading pair: Bitfinex API uses the prefix 't' for trading pairs
    // and 'f' for funding. we are only interested in the trading pairs, so prefix a 't'
    // for all tradingPairs requested
    private fun addPrefixToTradingPair(tradingPair: String) : String {
        return "t$tradingPair"
    }

    class BitfinexApiError(message: String, cause: Throwable) : Throwable(message, cause)
}