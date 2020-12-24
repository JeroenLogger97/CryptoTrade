package com.example.cryptotrade.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import com.example.cryptotrade.databinding.FragmentMarketBinding
import com.example.cryptotrade.model.HistoryResponse
import com.example.cryptotrade.model.TickerResponse
import com.example.cryptotrade.vm.TickerViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.time.temporal.Temporal
import java.time.temporal.TemporalUnit
import java.util.*
import kotlin.concurrent.fixedRateTimer

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MarketFragment : Fragment() {

    private val doRefresh = false

    // the time in ms the app should wait before sending a new API request to update the UI
    private val refreshTimeInMillis: Long = 10_000
    private val initialDelayInMillis: Long = 5_000

    private val viewModel: TickerViewModel by activityViewModels()
    private var _binding: FragmentMarketBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMarketBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding?.btnRefresh?.setOnClickListener {
            refreshHistory()
        }

        initViews()

        // initialize data on load
        viewModel.getTicker("BTCEUR")

        startRefreshTimer()
    }

    private fun startRefreshTimer() {
        if (doRefresh) {
            fixedRateTimer("refreshApiData", false, initialDelayInMillis, refreshTimeInMillis) {
                refreshTicker()
            }
        }
    }

    private fun initViews() {
        observeTicker()
        observeHistory()
        observeError()
    }

    private fun observeTicker() {
        viewModel.ticker.observe(viewLifecycleOwner, {
            binding.tvLastPrice.text = it.toString()
        })
    }

    private fun observeHistory() {
        viewModel.history.observe(viewLifecycleOwner, {
            binding.tvHistory.text = it.toString()
        })
    }

    private fun observeError() {
        viewModel.errorText.observe(viewLifecycleOwner, {
            Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
        })
    }

    private fun refreshTicker() {
        viewModel.getTicker("BTCEUR")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun refreshHistory() {
        val from: Long = LocalDateTime.now()
            .minus(7, ChronoUnit.DAYS)
            .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        // take a period of 30 minutes, we limit on 5 so we only get 5 back from the API.
        // we do need to make sure there is at least some data retrieved though, so that's why
        // we get data from 30 minutes to make sure there is at least one transaction and price
        val to: Long = from + (30 * 60 * 1_000)

        viewModel.getHistory("BTCUSD", from, to)
    }
}