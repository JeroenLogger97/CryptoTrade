package com.example.cryptotrade.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptotrade.R
import com.example.cryptotrade.model.database.Cryptocurrency
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.item_filter.view.*

class FilterAdapter(private val filters: List<Cryptocurrency>,//todo instead of filters: use cryptoC.values(): more accurate
                    private val onClick: (Cryptocurrency, Boolean) -> Unit,
                    private val selectedFilters: LiveData<HashSet<Cryptocurrency>>) : RecyclerView.Adapter<FilterAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.filterChip.setOnCloseIconClickListener {
                it.filterChip.isChecked = false
            }

            itemView.filterChip.setOnCheckedChangeListener { chip, isChecked ->
                val chipObj = chip as Chip
                chipObj.isCloseIconVisible = isChecked

                onClick(Cryptocurrency.valueOf(chipObj.text.toString()), isChecked)
            }
        }

        fun databind(filter: Cryptocurrency) {
            itemView.filterChip.text = filter.toString()

            val selectedFiltersValue = selectedFilters.value

            if (selectedFiltersValue != null && selectedFiltersValue.contains(filter)) {
                itemView.filterChip.isChecked = true
            }
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