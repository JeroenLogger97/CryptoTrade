package com.example.cryptotrade.repository

import android.content.Context
import com.example.cryptotrade.dao.TradingTransactionDao
import com.example.cryptotrade.database.CryptoTradeDatabase
import com.example.cryptotrade.model.database.TradingTransaction

class TradingTransactionRepository(context: Context) {

    private val tradingTransactionDao: TradingTransactionDao

    init {
        val database = CryptoTradeDatabase.getDatabase(context)
        tradingTransactionDao = database!!.tradingTransactionDao()
    }

    suspend fun getAllTradingTransactions() : List<TradingTransaction> {
        return tradingTransactionDao.getAllTradingTransactions()
    }

    suspend fun insertTradingTransaction(tradingTransaction: TradingTransaction) {
        tradingTransactionDao.insertTradingTransaction(tradingTransaction)
    }
}