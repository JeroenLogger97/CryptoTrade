package com.example.cryptotrade.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptotrade.R
import com.example.cryptotrade.model.MultipleTickersResponse
import com.example.cryptotrade.model.Ticker
import com.example.cryptotrade.repository.TradingPairRepository
import com.example.cryptotrade.util.Constants
import kotlinx.android.synthetic.main.item_market_pair.view.*

class TickerAdapter(private val liveDataToObserve: LiveData<MultipleTickersResponse>, private val lifecycleOwner: LifecycleOwner) : RecyclerView.Adapter<TickerAdapter.ViewHolder>() {

    private var buyListener: ((item: Ticker) -> Unit)? = null
    private var sellListener: ((item: Ticker) -> Unit)? = null

    fun setOnItemClickBuyListener(listener: (item: Ticker) -> Unit) {
        this.buyListener = listener
    }

    fun setOnItemClickSellListener(listener: (item: Ticker) -> Unit) {
        this.sellListener = listener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            //called once for each recyclerview entry
            Log.d(Constants.TAG, "initialize viewholder, set live data observe")

            liveDataToObserve.observe(lifecycleOwner) {
                Log.d(Constants.TAG, "LIVE DATA UPDATED IN TICKER ADAPTER")
            }
        }

        fun databind(ticker: Ticker) {
            // todo:
            //  - cache old price and flash price green or red depending on a rise or fall
            //  - call 24h price once in init block and cache the result, after that read from cache (start of day, 00:00)
            itemView.tvPair.text = ticker.symbol
            itemView.tvPrice.text = ticker.lastPrice.toString()
            itemView.tv24hChange.text = "+ 5.1% (TODO)"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TickerAdapter.ViewHolder {
        return ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_market_pair, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TickerAdapter.ViewHolder, position: Int) {
        val value = liveDataToObserve.value?.tickers?.get(position)
        value?.let { holder.databind(it) }

        holder.itemView.btnBuy.setOnClickListener {
            Log.d(Constants.TAG, "clicked buy for $value")
            if (value != null) {
                buyListener?.invoke(value)
            }
        }

        holder.itemView.btnSell.setOnClickListener {
            Log.d(Constants.TAG, "clicked sell for $value")
            if (value != null) {
                sellListener?.invoke(value)
            }
        }
    }

    override fun getItemCount(): Int {
        return TradingPairRepository().getAll().size
    }
}