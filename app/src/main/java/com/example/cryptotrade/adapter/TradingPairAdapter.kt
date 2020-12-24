package com.example.cryptotrade.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptotrade.R
import com.example.cryptotrade.model.TickerResponse
import com.example.cryptotrade.util.Constants
import kotlinx.android.synthetic.main.item_market_pair.view.*

// https://stackoverflow.com/questions/61364874/view-models-for-recyclerview-items
// https://stackoverflow.com/questions/47941537/notify-observer-when-item-is-added-to-list-of-livedata
class TradingPairAdapter(private val liveDataToObserve: LiveData<TickerResponse>, private val lifecycleOwner: LifecycleOwner) : RecyclerView.Adapter<TradingPairAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            liveDataToObserve.observe(lifecycleOwner) {
                Log.d(Constants.TAG, "LIVE DATA UPDATED IN TRADINGPAIRADAPTER")
            }
        }

        fun databind(ticker: TickerResponse) {
            Log.d(Constants.TAG, "databind called on $ticker")
            itemView.tvPrice.text = ticker.lastPrice.toString()
            itemView.tv24hChange.text = "+ 5.1% (TODO)"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TradingPairAdapter.ViewHolder {
        return ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_market_pair, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TradingPairAdapter.ViewHolder, position: Int) {
        liveDataToObserve.value?.let { holder.databind(it) }
    }

    override fun getItemCount(): Int {
        return 1
    }
}