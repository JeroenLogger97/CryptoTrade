package com.example.cryptotrade.api.converter

import android.util.Log
import com.example.cryptotrade.model.HistoryResponse
import com.example.cryptotrade.util.Constants
import okhttp3.ResponseBody
import retrofit2.Converter

class HistoryResponseBodyConverter : Converter<ResponseBody, HistoryResponse> {

    // sample response (BTCUSD):
    // [[543882359,1608207692713,-0.005,22690],
    //  [543882357,1608207691623,0.005,22693.18018077],
    //  [543882303,1608207691280,-0.0002,22695.18997088],
    //  [543882305,1608207691280,-0.0002,22695],
    //  [543882306,1608207691280,-0.0002,22694.686128]]
    override fun convert(value: ResponseBody): HistoryResponse? {
        val responseAsString = value.string()

        val firstMatch = "\\[\\[.*?]".toRegex().find(responseAsString)?.value
                ?: throw IllegalArgumentException("response body of history request is not in expected format")

        Log.d(Constants.TAG, "response: $responseAsString")
        Log.d(Constants.TAG, "match: $firstMatch")

        val values = firstMatch.substring(2, firstMatch.length - 1).split(",")

        // ignore the first value: we don't care about transaction id
        return HistoryResponse(values[1].toLong(),
                values[2].toDouble(),
                values[3].toDouble())
    }
}