package com.example.cryptotrade.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "portfolioEntry")
data class PortfolioEntry(
        @PrimaryKey
        val cryptocurrency: Cryptocurrency,
        val amount: Double
)