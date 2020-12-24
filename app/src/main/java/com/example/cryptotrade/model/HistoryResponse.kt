package com.example.cryptotrade.model

data class HistoryResponse(
    val timestamp: Long,
    val amount: Double,
    val price: Double
)