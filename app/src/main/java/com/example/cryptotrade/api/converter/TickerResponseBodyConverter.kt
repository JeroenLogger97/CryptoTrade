package com.example.cryptotrade.api.converter

import android.util.Log
import com.example.cryptotrade.model.TickerResponse
import okhttp3.ResponseBody
import retrofit2.Converter

class TickerResponseBodyConverter : Converter<ResponseBody, TickerResponse> {

    // sample response (BTCUSD):
    // [23180,
    //  14.94961206,
    //  23181,
    //  26.117962140000007,
    //  -682.59051648,
    //  -0.0286,
    //  23180,
    //  8030.49607643,
    //  23888,
    //  22655]
    override fun convert(value: ResponseBody): TickerResponse? {
        val responseAsString = value.string()

        Log.d("TAG", "convert responseBody value::: $responseAsString")

        val trimmed = responseAsString.substring(1, responseAsString.length - 1)
        val values = arrayListOf<Double>()

        val trimmedArray = trimmed.split(",")
        for (field in trimmedArray) {
            values.add(field.toDouble())
        }

        return TickerResponse(
                values[0],
                values[1],
                values[2],
                values[3],
                values[4],
                values[5],
                values[6],
                values[7],
                values[8],
                values[9]
        )
    }

}