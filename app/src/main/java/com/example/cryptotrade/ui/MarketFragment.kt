package com.example.cryptotrade.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.cryptotrade.R
import com.example.cryptotrade.databinding.FragmentMarketBinding
import com.example.cryptotrade.model.TickerResponse
import com.example.cryptotrade.repository.BitfinexRepository
import com.example.cryptotrade.vm.TickerViewModel
import kotlinx.coroutines.*
import java.util.*
import kotlin.concurrent.fixedRateTimer

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MarketFragment : Fragment() {

    // todo: set refresh time to something accepted, like 10_000
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeTicker()

        // call first time manually to ensure initial data is present
        viewModel.getTicker("BTCEUR")

        _binding?.btnRefresh?.setOnClickListener {
//            refreshData()
//
//            val tickerResponse = TickerResponse.createFromResponse(viewModel.ticker.value.toString())
//            Log.d("TAG", "$tickerResponse")
        }

        fixedRateTimer("refreshApiData", false, initialDelayInMillis, refreshTimeInMillis) {
            refreshData()
        }
    }

    private fun observeTicker() {
        viewModel.ticker.observe(viewLifecycleOwner, {
            binding.tvLastPrice.text = it
        })

        // Observe the error message.
        viewModel.errorText.observe(viewLifecycleOwner, {
            Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
        })
    }

    private fun refreshData() {
        viewModel.getTicker("BTCEUR")

        val tickerResponse = TickerResponse.createFromResponse(viewModel.ticker.value.toString())
        Log.d("TAG", "${Date()}: $tickerResponse")
    }
}