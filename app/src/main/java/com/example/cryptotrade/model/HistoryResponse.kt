package com.example.cryptotrade.model

import java.util.*

data class HistoryResponse(
    val timestamp: Long,
    val amount: Double,
    val price: Double
) {
    companion object {

        fun createFromResponse(responseAsString: String): HistoryResponse {
            return HistoryResponse(Date().time, 15.8, 4.1)
        }
    }
}