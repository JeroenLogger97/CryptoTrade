package com.example.cryptotrade.util

import androidx.room.TypeConverter
import com.example.cryptotrade.model.database.Action
import com.example.cryptotrade.model.database.Cryptocurrency
import java.util.*

class Converters {

    @TypeConverter
    fun fromAction(action: Action) : String {
        return action.toString()
    }

    @TypeConverter
    fun stringToAction(value: String) : Action {
        return Action.valueOf(value)
    }

    @TypeConverter
    fun fromTimestamp(value: Long) : Date {
        return Date(value)
    }

    @TypeConverter
    fun toTimestamp(date: Date) : Long {
        return date.time.toLong()
    }

    @TypeConverter
    fun fromCryptocurrency(value: Cryptocurrency) : String {
        return value.toString()
    }

    @TypeConverter
    fun toCryptocurrency(value: String) : Cryptocurrency {
        return Cryptocurrency.valueOf(value)
    }


}