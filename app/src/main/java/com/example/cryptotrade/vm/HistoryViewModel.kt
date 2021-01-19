package com.example.cryptotrade.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.cryptotrade.model.database.Cryptocurrency
import com.example.cryptotrade.repository.FilterRepository
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val filterRepository = FilterRepository()

    // can also add a LiveData set of higher order functions so we can not only filter on
    // cryptocurrency, but every field from TradingTransaction (for HistoryFragment)
    val selectedFilters: LiveData<HashSet<Cryptocurrency>> = filterRepository.selectedFilters

    fun addFilter(cryptocurrencyFilter: Cryptocurrency) {
        viewModelScope.launch {
            filterRepository.addFilter(cryptocurrencyFilter)
        }
    }

    fun removeFilter(cryptocurrencyFilter: Cryptocurrency) {
        viewModelScope.launch {
            filterRepository.removeFilter(cryptocurrencyFilter)
        }
    }
}