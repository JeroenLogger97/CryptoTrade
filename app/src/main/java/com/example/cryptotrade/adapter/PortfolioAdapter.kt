package com.example.cryptotrade.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptotrade.R
import com.example.cryptotrade.ui.PortfolioFragment
import kotlinx.android.synthetic.main.item_portfolio_entry.view.*
import java.util.*

class PortfolioAdapter(private val portfolioEntries: List<PortfolioFragment.PortfolioEntryValue>) : RecyclerView.Adapter<PortfolioAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun databind(portfolioEntry: PortfolioFragment.PortfolioEntryValue) {
            itemView.tvCurrency.text = portfolioEntry.portfolioEntry.cryptocurrency.toString()
            itemView.tvAmount.text = String.format(Locale.ENGLISH, "%.8f", portfolioEntry.portfolioEntry.amount)
            itemView.tvCurrentValue.text = String.format(Locale.ENGLISH, "$%.2f", portfolioEntry.currentValue)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PortfolioAdapter.ViewHolder {
        return ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_portfolio_entry, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PortfolioAdapter.ViewHolder, position: Int) {
        holder.databind(portfolioEntries[position])
    }

    override fun getItemCount(): Int {
        return portfolioEntries.size
    }
}