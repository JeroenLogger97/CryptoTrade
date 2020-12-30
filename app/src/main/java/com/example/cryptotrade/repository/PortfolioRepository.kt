package com.example.cryptotrade.repository

import android.content.Context
import com.example.cryptotrade.dao.PortfolioDao
import com.example.cryptotrade.database.CryptoTradeDatabase
import com.example.cryptotrade.model.database.Cryptocurrency
import com.example.cryptotrade.model.database.PortfolioEntry

class PortfolioRepository(context: Context) {

    private val portfolioDao: PortfolioDao

    init {
        val database = CryptoTradeDatabase.getDatabase(context)
        portfolioDao = database!!.portfolioEntryDao()
    }

    suspend fun getAllPortfolioEntries() : List<PortfolioEntry> {
        return portfolioDao.getAllPortfolioEntries()
    }

    suspend fun getPortfolioEntry(cryptocurrency: Cryptocurrency) : PortfolioEntry? {
        return portfolioDao.getPortfolioEntry(cryptocurrency)
    }

    suspend fun insertPortfolioEntry(portfolioEntry: PortfolioEntry) {
        portfolioDao.insertPortfolioEntry(portfolioEntry)
    }
}