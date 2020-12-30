package com.example.cryptotrade.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cryptotrade.model.database.Cryptocurrency
import com.example.cryptotrade.model.database.PortfolioEntry

@Dao
interface PortfolioDao {

    @Query("SELECT * FROM portfolioEntry")
    suspend fun getAllPortfolioEntries() : List<PortfolioEntry>

    @Query("SELECT * FROM portfolioEntry WHERE cryptocurrency = :cryptocurrency")
    suspend fun getPortfolioEntry(cryptocurrency: Cryptocurrency) : PortfolioEntry

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPortfolioEntry(portfolioEntry: PortfolioEntry)
}