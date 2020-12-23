package com.example.cryptotrade.model

data class TickerResponse(
    val bid: Double,
    val bidSize: Double,
    val ask: Double,
    val askSize: Double,
    val dailyChange: Double,
    val dailyChangeRelative: Double,
    val lastPrice: Double,
    val volume: Double,
    val high: Double,
    val low: Double
) {
    companion object {

        // the API will return the 10 fields described above
        fun createFromResponse(responseAsString: String) : TickerResponse {
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
}