package com.example.cryptotrade.model.database

data class PriceHistory(
        val tradingPair: String,
        val price: Double,
        val timestamp: Long
)