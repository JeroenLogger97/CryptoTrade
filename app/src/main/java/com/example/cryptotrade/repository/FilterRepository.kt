package com.example.cryptotrade.repository

import androidx.lifecycle.MutableLiveData
import com.example.cryptotrade.model.database.Cryptocurrency

class FilterRepository {

    val selectedFilters = MutableLiveData<HashSet<Cryptocurrency>>()

    private val selectedFiltersTracker = HashSet<Cryptocurrency>()

    fun addFilter(filter: Cryptocurrency) {
        selectedFiltersTracker.add(filter)
        selectedFilters.value = selectedFiltersTracker
    }

    fun removeFilter(filter: Cryptocurrency) {
        selectedFiltersTracker.remove(filter)
        selectedFilters.value = selectedFiltersTracker
    }
}