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
import com.example.cryptotrade.model.database.Cryptocurrency
import com.example.cryptotrade.repository.TradingPairRepository
import com.example.cryptotrade.util.Constants
import kotlinx.android.synthetic.main.item_market_pair.view.*
import kotlin.collections.HashMap

// todo: make priceAtStartOfDayLiveData custom object that has Map<Cryptocurrency, Double> with last prices per crypto
//  then every crypto has it's own last price
class TickerAdapter(private val tickersLiveData: LiveData<MultipleTickersResponse>,
                    private val priceAtStartOfDayLiveData: LiveData<HashMap<Cryptocurrency, Double>>,
                    private val lifecycleOwner: LifecycleOwner) : RecyclerView.Adapter<TickerAdapter.ViewHolder>() {

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
            Log.d(Constants.TAG, "TickerAdapter :: initialize viewholder, set live data observe")

            tickersLiveData.observe(lifecycleOwner) {
                Log.d(Constants.TAG, "TickerAdapter :: tickers updated")
            }

            priceAtStartOfDayLiveData.observe(lifecycleOwner) {
                Log.d(Constants.TAG, "TickerAdapter :: price at start of day updated to ${priceAtStartOfDayLiveData.value}")
//                priceAtStartOfDay = priceAtStartOfDayLiveData.value!!
            }
        }

        fun databind(ticker: Ticker) {
            // todo:
            //  - cache old price and flash price green or red depending on a rise or fall
            itemView.tvPair.text = ticker.symbol

            // todo: if total length of last price is longer then x: round decimal places to 2
            itemView.tvPrice.text = ticker.lastPrice.toString()

            val cryptocurrency = Cryptocurrency.fromTradingPair(ticker.symbol)
            val change = priceAtStartOfDayLiveData.value?.get(cryptocurrency)?.let { calculateChange(it, ticker.lastPrice) }

            itemView.tv24hChange.text = "$change%"
        }
    }

    private fun calculateChange(start: Double, current: Double) : Double {
        val change = (current - start) / start * 100
        return String.format("%.2f", change).toDouble()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TickerAdapter.ViewHolder {
        return ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_market_pair, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TickerAdapter.ViewHolder, position: Int) {
        val value = tickersLiveData.value?.tickers?.get(position)
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