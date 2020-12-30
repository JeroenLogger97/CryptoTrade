package com.example.cryptotrade.database

import android.content.Context
import androidx.room.*
import com.example.cryptotrade.dao.PortfolioDao
import com.example.cryptotrade.dao.TradingTransactionDao
import com.example.cryptotrade.model.database.PortfolioEntry
import com.example.cryptotrade.model.database.TradingTransaction
import com.example.cryptotrade.util.Converters

@Database(entities = [TradingTransaction::class, PortfolioEntry::class], version = 3, exportSchema = false)
@TypeConverters(Converters::class)
abstract class CryptoTradeDatabase : RoomDatabase() {

    abstract fun tradingTransactionDao(): TradingTransactionDao
    abstract fun portfolioEntryDao(): PortfolioDao

    companion object {
        private const val DATABASE_NAME = "CRYPTO_TRADE_DATABASE"

        @Volatile
        private var roomDatabaseInstance: CryptoTradeDatabase? = null

        fun getDatabase(context: Context): CryptoTradeDatabase? {
            if (roomDatabaseInstance == null) {
                synchronized(CryptoTradeDatabase::class.java) {
                    if (roomDatabaseInstance == null) {
                        roomDatabaseInstance =
                                Room.databaseBuilder(
                                        context.applicationContext,
                                        CryptoTradeDatabase::class.java,
                                        DATABASE_NAME
                                )
                                        .fallbackToDestructiveMigration()
                                        .build()
                    }
                }
            }
            return roomDatabaseInstance
        }
    }

}