package com.example.cryptotrade.model.database

//todo: remove class, since the already getting 24h price on load of market fragment
data class PriceHistory(
        val tradingPair: String,
        val price: Double,
        val timestamp: Long
)