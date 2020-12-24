package com.example.cryptotrade.api.converter

import android.util.Log
import com.example.cryptotrade.model.TickerResponse
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
        Log.d("TAG", "requested type:       $type")
        Log.d("TAG", "ticker response type: ${TickerResponse::class.java}")

        if (type == TickerResponse::class.java) {
            return TickerResponseBodyConverter()
        }

        return null
    }
}