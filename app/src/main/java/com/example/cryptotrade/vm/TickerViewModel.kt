package com.example.cryptotrade.vm

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.cryptotrade.repository.BitfinexRepository
import kotlinx.coroutines.launch

class TickerViewModel(application: Application) : AndroidViewModel(application){

    private val bitfinexRepository = BitfinexRepository()

    val ticker = bitfinexRepository.ticker

    private val _errorText: MutableLiveData<String> = MutableLiveData()

    val errorText: LiveData<String>
        get() = _errorText

    fun getTicker(tradingPair: String) {
        viewModelScope.launch {
            try {
                // add a 't' before trading pair: Bitfinex API uses the prefix 't' for trading pairs
                // and 'f' for funding. we are only interested in the trading pairs, so prefix a 't'
                // for all tradingPairs requested
                bitfinexRepository.getTicker("t$tradingPair")
            } catch (error: BitfinexRepository.BitfinexApiError) {
                _errorText.value = error.message
                Log.e("Bitfinex API error", error.cause.toString())
            }
        }
    }
}