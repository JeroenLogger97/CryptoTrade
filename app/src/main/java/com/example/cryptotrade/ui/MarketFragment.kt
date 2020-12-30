package com.example.cryptotrade.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptotrade.adapter.TickerAdapter
import com.example.cryptotrade.databinding.FragmentMarketBinding
import com.example.cryptotrade.model.Ticker
import com.example.cryptotrade.model.database.Cryptocurrency
import com.example.cryptotrade.repository.TradingPairRepository
import com.example.cryptotrade.repository.TradingTransactionRepository
import com.example.cryptotrade.util.Constants
import com.example.cryptotrade.vm.TickerViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_market.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.concurrent.fixedRateTimer

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MarketFragment : Fragment() {

    private val doRefresh = true

    // the time in ms the app should wait before sending a new API request to update the UI
    private val refreshTimeInMillis: Long = 10_000
    private val initialDelayInMillis: Long = 5_000

    private val viewModel: TickerViewModel by activityViewModels()
    private var _binding: FragmentMarketBinding? = null
    private val binding get() = _binding!!

    private lateinit var tickerAdapter: TickerAdapter
    private val tradingPairRepository = TradingPairRepository()

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

        tickerAdapter = TickerAdapter(viewModel.tickers, viewModel.pricesAtStartOfDay, viewLifecycleOwner)

        initViews()

        // initialize data on load
        refreshTickers()
        refreshPriceAtStartOfDay()

        startRefreshTimer()

        tickerAdapter.setOnItemClickBuyListener {
            showDialogFragment(BottomSheetBuyFragment(), it)
        }

        tickerAdapter.setOnItemClickSellListener {
            showDialogFragment(BottomSheetSellFragment(), it)
        }

    }

    // used for Buy and Sell bottom sheet fragments
    private fun showDialogFragment(fragment: BottomSheetDialogFragment, ticker: Ticker) {
        val bundle = Bundle()
        bundle.putString(SYMBOL_KEY, ticker.symbol)
        bundle.putDouble(PRICE_KEY, ticker.lastPrice)

        fragment.arguments = bundle
        fragment.show(parentFragmentManager, fragment.tag)
    }

    private fun initViews() {
        observeTickers()
        observeError()

        rvTradingPairs.adapter = tickerAdapter

        rvTradingPairs.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        tickerAdapter.notifyDataSetChanged()
    }

    private fun startRefreshTimer() {
        if (doRefresh) {
            fixedRateTimer("refreshApiData", false, initialDelayInMillis, refreshTimeInMillis) {
                refreshTickers()
            }
        }
    }

    private fun observeTickers() {
        viewModel.tickers.observe(viewLifecycleOwner, {
            var output = ""
            for (ticker in it.tickers) {
                output += ticker.symbol + ": " + ticker.lastPrice + "\n"
            }

            binding.tvLastPrice.text = output

            tickerAdapter.notifyDataSetChanged()
        })
    }

    private fun observeError() {
        viewModel.errorText.observe(viewLifecycleOwner, {
            Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
        })
    }

    private fun refreshTickers() {
        viewModel.getTickers(*tradingPairRepository.getAll().toTypedArray())
    }

    private fun refreshPriceAtStartOfDay() {
        // todo: call this once every x hours
        //  or in bitfinex repository: get value of static '24h' point:
        //  example: time is 13:31, get 24h price at 13:00
        for (pair in tradingPairRepository.getAll()) {
            viewModel.getPriceAtStartOfDay(pair)
        }
    }
}