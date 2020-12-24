package com.example.cryptotrade.repository

class TradingPairRepository {

    fun getAll() : List<String> {
        return listOf("BTCUSD", "LTCUSD")
    }

}