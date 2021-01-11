package com.example.cryptotrade.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptotrade.R
import com.example.cryptotrade.model.database.TradingTransaction
import kotlinx.android.synthetic.main.item_trading_transaction.view.*
import java.text.SimpleDateFormat

class TradingTransactionAdapter(private val tradingTransactions: List<TradingTransaction>) : RecyclerView.Adapter<TradingTransactionAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun databind(tradingTransaction: TradingTransaction) {
            itemView.tvTradingPair.text = tradingTransaction.tradingPair
            itemView.tvTradingAmount.text = String.format("%.8f", tradingTransaction.amount)
            itemView.tvPriceAtTrade.text = String.format("$%.2f", tradingTransaction.price)
            itemView.tvActionType.text = tradingTransaction.action.toString()
            itemView.tvActionType.setTextColor(tradingTransaction.action.color)
            itemView.tvTradeDate.text = SimpleDateFormat("dd-MM-yyyy, HH:mm:ss").format(tradingTransaction.timestamp).toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TradingTransactionAdapter.ViewHolder {
        return ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_trading_transaction, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TradingTransactionAdapter.ViewHolder, position: Int) {
        holder.databind(tradingTransactions[position])
    }

    override fun getItemCount(): Int {
        return tradingTransactions.size
    }
}