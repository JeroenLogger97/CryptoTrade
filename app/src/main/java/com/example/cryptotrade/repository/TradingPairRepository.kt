package com.example.cryptotrade.repository

import com.example.cryptotrade.model.database.Cryptocurrency
import com.example.cryptotrade.model.database.FiatCurrency

class TradingPairRepository {

    fun getAll() : List<String> {
        val coins = ArrayList<String>()

        for (value in Cryptocurrency.values()) {
            coins.add(value.toString() + FiatCurrency.USD.toString())
        }

        return coins
    }

}