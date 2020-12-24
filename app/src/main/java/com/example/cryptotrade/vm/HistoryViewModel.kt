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

class HistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val bitfinexRepository = BitfinexRepository()

    val history = bitfinexRepository.history

    private val _errorText: MutableLiveData<String> = MutableLiveData()

    val errorText: LiveData<String>
        get() = _errorText

    fun getHistory(tradingPair: String,
                   startInMillis: Long,
                   endInMillis: Long) {
        viewModelScope.launch {
            try {
                bitfinexRepository.getHistory(tradingPair,
                        startInMillis.toString(), endInMillis.toString())
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