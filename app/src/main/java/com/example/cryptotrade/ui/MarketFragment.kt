package com.example.cryptotrade.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import com.example.cryptotrade.adapter.TradingPairAdapter
import com.example.cryptotrade.databinding.FragmentMarketBinding
import com.example.cryptotrade.model.TickerResponse
import com.example.cryptotrade.vm.TickerViewModel
import kotlinx.android.synthetic.main.fragment_market.*
import kotlinx.coroutines.channels.ticker
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

    private lateinit var tickerAdapter: TradingPairAdapter

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMarketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        tickerAdapter = TradingPairAdapter(viewModel.ticker, viewLifecycleOwner)

        initViews()

        // initialize data on load
        viewModel.getTicker("BTCEUR")

        startRefreshTimer()
    }

    private fun initViews() {
        observeTicker()
        observeError()

//        rvTradingPairs.adapter = tickerAdapter
//
//        tickerAdapter.notifyDataSetChanged()
    }

    private fun startRefreshTimer() {
        if (doRefresh) {
            fixedRateTimer("refreshApiData", false, initialDelayInMillis, refreshTimeInMillis) {
                refreshTicker()
            }
        }
    }

    private fun observeTicker() {
        viewModel.ticker.observe(viewLifecycleOwner, {
            binding.tvLastPrice.text = it.toString()
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
}