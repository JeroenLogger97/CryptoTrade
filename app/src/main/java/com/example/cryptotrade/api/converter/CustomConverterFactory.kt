package com.example.cryptotrade.api.converter

import com.example.cryptotrade.model.HistoryResponse
import com.example.cryptotrade.model.MultipleTickersResponse
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class CustomConverterFactory : Converter.Factory() {

    companion object {
        fun create() : CustomConverterFactory {
            return CustomConverterFactory()
        }
    }

    override fun responseBodyConverter(type: Type, annotations: Array<Annotation>, retrofit: Retrofit): Converter<ResponseBody, *>? {
        return when (type) {
            HistoryResponse::class.java -> {
                HistoryResponseBodyConverter()
            }
            MultipleTickersResponse::class.java -> {
                MultipleTickersResponseBodyConverter()
            }
            else -> null
        }

    }
}