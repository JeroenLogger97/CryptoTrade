package com.example.cryptotrade.model.database

/**
 * All supported crypto currencies. The app trades all these pairs versus USD, so BTC will become
 * trading pair BTCUSD
 */
enum class Cryptocurrency {
    BTC, LTC, XRP, ZEC, ETH, NEO;

    companion object {
        fun fromTradingPair(tradingPair: String) : Cryptocurrency {
            return valueOf(tradingPair.substring(0, 3))
        }
    }
}