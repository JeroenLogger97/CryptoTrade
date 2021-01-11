package com.example.cryptotrade.model.database

import android.graphics.Color

//todo color is hardcoded: how to get R.color.green?
enum class Action(val color: Int) {
    BUY(Color.parseColor( "#8A12B600")),
    SELL(Color.parseColor("#AEC11919"));
    // green: #8A12B600
    // red:   #AEC11919
}