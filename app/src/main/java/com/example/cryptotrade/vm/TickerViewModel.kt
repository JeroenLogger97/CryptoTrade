package com.example.cryptotrade.vm

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.cryptotrade.repository.BitfinexRepository
import com.example.cryptotrade.util.Constants
import kotlinx.coroutines.launch

class TickerViewModel(application: Application) : AndroidViewModel(application) {

    private val bitfinexRepository = BitfinexRepository()

    val tickers = bitfinexRepository.tickers
    val pricesAtStartOfDay = bitfinexRepository.pricesAtStartOfDay

    private val _errorText: MutableLiveData<String> = MutableLiveData()

    val errorText: LiveData<String>
        get() = _errorText

    fun getTickers(vararg tradingPairs: String) {
        viewModelScope.launch {
            try {
                bitfinexRepository.getMultipleTickers(*tradingPairs)
            } catch (error: BitfinexRepository.BitfinexApiError) {
                onError(error)
            }
        }
    }

    fun getPriceAtStartOfDay(tradingPair: String) {
        viewModelScope.launch {
            try {
                bitfinexRepository.getPriceAtStartOfDay(tradingPair)
            } catch (error: BitfinexRepository.BitfinexApiError) {
                onError(error)
            }
        }
    }

    private fun onError(error: BitfinexRepository.BitfinexApiError) {
        _errorText.value = error.message
        Log.e(Constants.TAG, "Bitfinex API error: ${error.cause.toString()}")
    }
}