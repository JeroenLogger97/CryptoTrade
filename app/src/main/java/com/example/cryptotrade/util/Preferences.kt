package com.example.cryptotrade.util

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

const val SHARED_PREFERENCES_NAME = "shared_preferences"
const val KEY_LAST_VERSION_RUN = "LAST_VERSION_RUN"
const val DEFAULT_LAST_VERSION_RUN = -1 // if no last version is present
const val KEY_USD_BALANCE = "usd_balance"

class Preferences(context: Context) {

    private val sharedPreferences: SharedPreferences by lazy { context.getSharedPreferences(
            SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE) }

    fun getPreferences() : SharedPreferences {
        return sharedPreferences
    }

    fun setPreference(key: String, value: Any) {
        putValue(key, value)
    }

    private fun putValue(key: String, value: Any) {
        when (value) {
            is String -> {
                sharedPreferences.edit().putString(key, value).apply()
            }
            is Int -> {
                sharedPreferences.edit().putInt(key, value).apply()
            }
            is Boolean -> {
                sharedPreferences.edit().putBoolean(key, value).apply()
            }
            is Double -> {
                sharedPreferences.edit().putFloat(key, value.toFloat()).apply()
            }
            is Float -> {
                sharedPreferences.edit().putFloat(key, value).apply()
            }
            is Long -> {
                sharedPreferences.edit().putLong(key, value).apply()
            }
            else -> {
                Log.w(Constants.TAG, "Cannot put object of class ${value.javaClass} in sharedPreferences")
            }
        }
    }
}