package com.example.cryptotrade.api.converter

import com.example.cryptotrade.model.MultipleTickersResponse
import com.example.cryptotrade.model.Ticker
import okhttp3.ResponseBody
import retrofit2.Converter

class MultipleTickersResponseBodyConverter : Converter<ResponseBody, MultipleTickersResponse> {

    // sample response (BTCUSD, LTCUSD):
    // [["tBTCUSD",23227,21.791856230000004,23228,11.09254416,-364.80335253,-0.0155,23227.19664747,6368.72260788,23671.74173591,22655],
    //  ["tLTCUSD",107.08,1274.13734849,107.15,3059.2667714399995,1.18,0.0111,107.18,228870.16137935,109.52,95.47]]
    override fun convert(value: ResponseBody): MultipleTickersResponse? {
        val tickers: ArrayList<Ticker> = arrayListOf()

        val responseAsString = value.string()

        val trimmed = responseAsString.substring(1, responseAsString.length - 1)
        val splitTickers = "\\[.*?]".toRegex().findAll(trimmed)

        for (ticker in splitTickers) {
            val values = arrayListOf<Double>()
            var symbol = ""

            val trimmedArray = ticker.value.substring(0, ticker.value.length - 1).split(",")
            for ((index, field) in trimmedArray.withIndex()) {
                if (index == 0) {
                    // don't add first value to values, since that is a string
                    symbol = field
                    continue
                }
                values.add(field.toDouble())
            }

            tickers.add(
                Ticker(symbol,
                    values[0],
                    values[1],
                    values[2],
                    values[3],
                    values[4],
                    values[5],
                    values[6],
                    values[7],
                    values[8],
                    values[9]))
        }

        return MultipleTickersResponse(tickers)
    }
}