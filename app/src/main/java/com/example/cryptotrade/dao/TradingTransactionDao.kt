package com.example.cryptotrade.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.cryptotrade.model.database.TradingTransaction

@Dao
interface TradingTransactionDao {

    @Query("SELECT * FROM tradingTransaction ORDER BY id DESC")
    suspend fun getAllTradingTransactions() : List<TradingTransaction>

    @Insert
    suspend fun insertTradingTransaction(tradingTransaction: TradingTransaction)
}