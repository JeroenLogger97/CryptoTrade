package com.example.cryptotrade.model.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "tradingTransaction")
data class TradingTransaction(
        val amount: Double,
        val price: Double,
        val tradingPair: String,
        val action: Action,
        val timestamp: Date,

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        var id: Long? = null,
)