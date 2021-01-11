package com.example.cryptotrade.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptotrade.R
import com.example.cryptotrade.adapter.FilterAdapter
import com.example.cryptotrade.model.HistoryFilter
import com.example.cryptotrade.model.database.Cryptocurrency
import com.example.cryptotrade.util.Constants
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_history_filter.*

class HistoryFilterBottomSheet : BottomSheetDialogFragment() {

    //todo: when quiting fragment, send selected filters to history fragment? or set in a viewmodel and read from viewmodel in history fragment
//    private val cryptoFilters = arrayListOf<HistoryFilter>()
    private val cryptoFilters = arrayListOf<Cryptocurrency>()
    private val filterAdapter: FilterAdapter by lazy {
        FilterAdapter(cryptoFilters, ::onFilterClick)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_history_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews () {
        for (value in Cryptocurrency.values()) {
            cryptoFilters.add(value)
        }

        rvFilters.adapter = filterAdapter
        rvFilters.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

        filterAdapter.notifyDataSetChanged()
    }

    private fun onFilterClick(cryptocurrency: Cryptocurrency, isChecked: Boolean) {
        Log.d(Constants.TAG, "in fragment: $cryptocurrency: $isChecked")
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d(Constants.TAG, "CALLED ON DESTROY!!")
    }
}