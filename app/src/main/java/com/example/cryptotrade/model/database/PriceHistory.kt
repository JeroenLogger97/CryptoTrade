package com.example.cryptotrade.model.database

import java.time.LocalDateTime

data class PriceHistory(
        val tradingPair: String,
        val price: Double,
        val dateTime: LocalDateTime
)