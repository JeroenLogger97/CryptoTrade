package com.example.cryptotrade.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptotrade.adapter.TradingTransactionAdapter
import com.example.cryptotrade.databinding.FragmentHistoryBinding
import com.example.cryptotrade.model.database.Cryptocurrency
import com.example.cryptotrade.model.database.TradingTransaction
import com.example.cryptotrade.repository.TradingTransactionRepository
import com.example.cryptotrade.util.Constants
import com.example.cryptotrade.vm.HistoryViewModel
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*

class HistoryFragment : Fragment() {

    private val viewModel: HistoryViewModel by activityViewModels()
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val tradingTransactions: ArrayList<TradingTransaction> = arrayListOf()
    private val tradingTransactionAdapter = TradingTransactionAdapter(tradingTransactions)
    private val tradingTransactionRepository: TradingTransactionRepository by lazy { TradingTransactionRepository(requireContext()) }

    private var startMillis: Long = 0
    private var endMillis: Long = 0

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        observeFilters()
    }

    private fun observeFilters() {
        viewModel.selectedFilters.observe(viewLifecycleOwner, {
            tradingTransactions.clear()
            tradingTransactions.addAll(getTradingTransactions())

            filterTradingTransactionsOnCryptocurrency(it)
            filterTradingTransactionsOnStartAndEndMillis()
        })
    }

    private fun filterTradingTransactionsOnCryptocurrency(it: HashSet<Cryptocurrency>) {
        if (it.isEmpty()) {
            tradingTransactionAdapter.notifyDataSetChanged()
            return
        }

        val filteredTransactions = arrayListOf<TradingTransaction>()

        for (tradingTransaction in tradingTransactions) {
            if (it.contains(Cryptocurrency.fromTradingPair(tradingTransaction.tradingPair))) {
                filteredTransactions.add(tradingTransaction)
            }
        }

        tradingTransactions.clear()
        tradingTransactions.addAll(filteredTransactions)
        tradingTransactionAdapter.notifyDataSetChanged()
    }

    private fun filterTradingTransactionsOnStartAndEndMillis() {
        if (startMillis == 0L && endMillis == 0L) {
            return
        }

        val filteredTransactions = tradingTransactions.filter { transaction ->
            if (transaction.timestamp.time in startMillis..endMillis) {
                return@filter true
            }
            return@filter false
        }

        tradingTransactions.clear()
        tradingTransactions.addAll(filteredTransactions)
        tradingTransactionAdapter.notifyDataSetChanged()
    }

    private fun initViews() {
        tvDateFrom.text = "No date selected"
        tvDateTo.text = "No date selected"

        val calendar = Calendar.getInstance()

        val builder = MaterialDatePicker.Builder.dateRangePicker()
        builder.setSelection(Pair(calendar.timeInMillis, calendar.timeInMillis))
        builder.setTitleText("Select date range")

        val picker = builder.setCalendarConstraints(CalendarConstraints.Builder().build()).build()
        picker.addOnNegativeButtonClickListener {
            picker.dismiss()
        }
        picker.addOnPositiveButtonClickListener {
            tradingTransactions.clear()
            tradingTransactions.addAll(getTradingTransactions())

            startMillis = it.first!!

            val endDate = Date(it.second!!)
            val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
            val endDateFormatted = simpleDateFormat.format(endDate)
            val todayFormatted = simpleDateFormat.format(Date())
            Log.d(Constants.TAG, "end date: $endDateFormatted")
            Log.d(Constants.TAG, "today   : $todayFormatted")

            val endDateIsToday = endDateFormatted == todayFormatted
            val startAndEndAreSameDate = it.first!! == it.second!!

            endMillis = if (endDateIsToday || startAndEndAreSameDate) {
                // either the end date is today or the start and end date are the same date.
                // in both cases add one day to end date:
                // - end date is today: we want to include all transactions from today, so not
                //   until 00:00 but until 00:00 next day
                // - same date: we want all transactions from that date, not just from
                //   00:00 to 00:00 on the same date (which would of course not return anything)

                val cal = Calendar.getInstance()
                cal.time = endDate
                cal.add(Calendar.DATE, 1)

                cal.timeInMillis
            } else {
                it.second!!
            }

            tvDateFrom.text = simpleDateFormat.format(Date(startMillis))
            tvDateTo.text = simpleDateFormat.format(Date(endMillis))

            filterTradingTransactionsOnStartAndEndMillis()

            viewModel.selectedFilters.value?.let { filters -> filterTradingTransactionsOnCryptocurrency(filters) }
            tradingTransactionAdapter.notifyDataSetChanged()
        }

        btnSelectDateRange.setOnClickListener {
            picker.show(activity?.supportFragmentManager!!, builder.toString())
        }

        tradingTransactions.addAll(getTradingTransactions())

        // initialize recyclerview
        rvTradingTransactions.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvTradingTransactions.adapter = tradingTransactionAdapter
        tradingTransactionAdapter.notifyDataSetChanged()

        btnFilter.setOnClickListener {
            val historyFilterBottomSheet = HistoryFilterBottomSheet()
            historyFilterBottomSheet.show(requireActivity().supportFragmentManager, historyFilterBottomSheet.tag)
        }
    }

    // need this method to bridge between coroutine scopes and 'normal' code
    private fun getTradingTransactions() : List<TradingTransaction> {
        var tradingTransactions: List<TradingTransaction> = listOf()

        runBlocking {
            tradingTransactions = getTradingTransactionsFromDatabase()
        }

        return tradingTransactions
    }

    private suspend fun getTradingTransactionsFromDatabase() : List<TradingTransaction> {
        var returnValue: List<TradingTransaction> = arrayListOf()

        runBlocking {
            val transactions = async {
                tradingTransactionRepository.getAllTradingTransactions()
            }

            runBlocking {
                returnValue = transactions.await()
            }
        }

        return returnValue
    }
}