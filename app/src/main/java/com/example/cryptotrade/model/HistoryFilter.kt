package com.example.cryptotrade.model

import com.example.cryptotrade.model.database.Cryptocurrency

data class HistoryFilter(
        val cryptocurrency: Cryptocurrency,
        val selected: Boolean
)