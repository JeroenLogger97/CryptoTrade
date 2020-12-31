package com.example.cryptotrade.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptotrade.adapter.PortfolioAdapter
import com.example.cryptotrade.databinding.FragmentPortfolioBinding
import com.example.cryptotrade.model.MultipleTickersResponse
import com.example.cryptotrade.model.database.Cryptocurrency
import com.example.cryptotrade.model.database.PortfolioEntry
import com.example.cryptotrade.repository.PortfolioRepository
import com.example.cryptotrade.util.KEY_USD_BALANCE
import com.example.cryptotrade.util.Preferences
import com.example.cryptotrade.vm.TickerViewModel
import kotlinx.android.synthetic.main.fragment_portfolio.*
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class PortfolioFragment : Fragment() {

    private val preferences: Preferences by lazy { Preferences(requireContext()) }
    private val portfolioRepository: PortfolioRepository by lazy { PortfolioRepository(requireContext()) }

    private val viewModel: TickerViewModel by activityViewModels()
    private var _binding: FragmentPortfolioBinding? = null
    private val binding get() = _binding!!

    private val portfolioEntries: ArrayList<PortfolioEntryValue> = arrayListOf()
    private val portfolioAdapter = PortfolioAdapter(portfolioEntries)

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPortfolioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {
        val multipleTickersResponse = viewModel.tickers.value ?: MultipleTickersResponse(listOf())

        var totalCryptocurrencyValue = 0.0

        for (ticker in multipleTickersResponse.tickers) {
            val cryptocurrency = Cryptocurrency.fromTradingPair(ticker.symbol)
            val cryptoAmount = getAvailableCryptocurrency(cryptocurrency)

            if (cryptoAmount > 0.0) {
                val currentValue = ticker.lastPrice * cryptoAmount
                totalCryptocurrencyValue += currentValue

                portfolioEntries.add(PortfolioEntryValue(PortfolioEntry(cryptocurrency, cryptoAmount), currentValue))
            }
        }

        val usdBalanceValue = preferences.getPreferences().getFloat(KEY_USD_BALANCE, 0f)
        val usdBalanceString = String.format("%.2f", usdBalanceValue)

        val totalCryptocurrencyValueString = String.format("%.2f", totalCryptocurrencyValue)
        val totalValueString = String.format("%.2f", usdBalanceValue + totalCryptocurrencyValue)

        tvUsdBalance.text = "$$usdBalanceString"
        tvCryptocurrencyValue.text = "$$totalCryptocurrencyValueString"
        tvTotalValue.text = "$$totalValueString"

        rvPortfolio.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvPortfolio.adapter = portfolioAdapter
        portfolioAdapter.notifyDataSetChanged()
    }

    // need this method to bridge between coroutine scopes and 'normal' code
    private fun getAvailableCryptocurrency(cryptocurrency: Cryptocurrency) : Double {
        var availableCrypto = 0.0

        runBlocking {
            availableCrypto = getCryptoAmt(cryptocurrency)
        }

        return availableCrypto
    }

    private suspend fun getCryptoAmt(cryptocurrency: Cryptocurrency) : Double {
        var total: Double = 0.0

        runBlocking {
            val entry = async { getExistingPortfolioEntry(cryptocurrency) }

            runBlocking {
                total = entry.await().amount
            }
        }

        return total
    }

    private suspend fun getExistingPortfolioEntry(cryptocurrency: Cryptocurrency) : PortfolioEntry {
        return portfolioRepository.getPortfolioEntry(cryptocurrency) ?: PortfolioEntry(cryptocurrency, 0.0)
    }

    class PortfolioEntryValue(val portfolioEntry: PortfolioEntry, val currentValue: Double) {}
}