package com.example.cryptotrade.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptotrade.R
import com.example.cryptotrade.adapter.FilterAdapter
import com.example.cryptotrade.model.database.Cryptocurrency
import com.example.cryptotrade.util.Constants
import com.example.cryptotrade.vm.HistoryViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_history_filter.*

class HistoryFilterBottomSheet : BottomSheetDialogFragment() {

    private val viewModel: HistoryViewModel by activityViewModels()

    private lateinit var filterAdapter: FilterAdapter
    private val selectedFilters = arrayListOf<Cryptocurrency>()

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
        filterAdapter = FilterAdapter(listOf(*Cryptocurrency.values()), ::onFilterClick, viewModel.selectedFilters)

        rvFilters.adapter = filterAdapter
        rvFilters.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

        filterAdapter.notifyDataSetChanged()

        observeSelectedFilters()
    }

    private fun onFilterClick(cryptocurrency: Cryptocurrency, isChecked: Boolean) {
        if (isChecked) {
            selectedFilters.add(cryptocurrency)
            viewModel.addFilter(cryptocurrency)
        } else {
            selectedFilters.remove(cryptocurrency)
            viewModel.removeFilter(cryptocurrency)
        }
    }

    private fun observeSelectedFilters() {
        viewModel.selectedFilters.observe(viewLifecycleOwner, {
            Log.d(Constants.TAG, "filters: $it")
        })
    }
}