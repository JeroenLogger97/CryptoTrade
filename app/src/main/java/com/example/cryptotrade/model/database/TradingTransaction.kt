package com.example.cryptotrade.model.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp
import java.time.LocalDateTime

@Entity(tableName = "tradingTransaction")
data class TradingTransaction(
        val amount: Double,
        val tradingPair: String,
        val action: Action,
        val timestamp: Long,

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        var id: Long? = null,
)