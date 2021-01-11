package com.example.cryptotrade.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptotrade.R
import com.example.cryptotrade.model.HistoryFilter
import com.example.cryptotrade.model.database.Cryptocurrency
import com.example.cryptotrade.util.Constants
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.item_filter.view.*

class FilterAdapter(private val filters: List<Cryptocurrency>, private val onClick: (Cryptocurrency, Boolean) -> Unit) : RecyclerView.Adapter<FilterAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.filterChip.setOnClickListener {
//                Log.d(Constants.TAG, "clicked ${it.filterChip.text}")
//                it.filterChip.isChecked = true
//                it.filterChip.isCloseIconVisible = true
            }

            itemView.filterChip.setOnCloseIconClickListener {
//                Log.d(Constants.TAG, "closed ${it.filterChip.text}")
                it.filterChip.isChecked = false
//                it.filterChip.isCloseIconVisible = false
            }

            itemView.filterChip.setOnCheckedChangeListener { chip, isChecked ->
                Log.d(Constants.TAG, "changed ${chip.text} to $isChecked")

                val chipObj = chip as Chip
                chipObj.isCloseIconVisible = isChecked

                onClick(Cryptocurrency.valueOf(chipObj.text.toString()), isChecked)
            }
        }

        fun databind(filter: Cryptocurrency) {
            itemView.filterChip.text = filter.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterAdapter.ViewHolder {
        return ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_filter, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FilterAdapter.ViewHolder, position: Int) {
        holder.databind(filters[position])
    }

    override fun getItemCount(): Int {
        return filters.size
    }
}