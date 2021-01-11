package com.example.cryptotrade.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cryptotrade.R
import com.example.cryptotrade.model.database.Action
import com.example.cryptotrade.model.database.FiatCurrency
import com.example.cryptotrade.model.database.PortfolioEntry
import com.example.cryptotrade.model.database.TradingTransaction
import com.example.cryptotrade.util.KEY_USD_BALANCE
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.bottom_sheet_sell.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.*
import java.util.*

class BottomSheetSellFragment : MarketBottomSheetFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_sell, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvSellCryptocurrency.text = cryptocurrency.toString()

        btnPopupSell.setOnClickListener {
            var error = ""

            val availableCryptocurrency = getAvailableCryptocurrency()

            val cryptoAmount = etSellInputCrypto.text
            val totalPrice = etSellInputUsd.text.substring(1).toDouble()

            when {
                cryptoAmount.isEmpty() -> {
                    error = "Cryptocurrency amount is empty"
                }
                totalPrice == 0.0 -> {
                    error = "Sell price too small"
                }
                availableCryptocurrency < cryptoAmount.toString().toDouble() -> {
                    // not enough cryptocurrency in portfolio to sell
                    error = "Not enough $cryptocurrency! You only have $availableCryptocurrency $cryptocurrency"
                }
                else -> {
                    sellCrypto(cryptoAmount.toString().toDouble(), totalPrice)
                }
            }

            if (error.isNotEmpty()) {
                layout_main.view?.let { parentView ->
                    Snackbar.make(parentView, error, Snackbar.LENGTH_SHORT).show()
                }
            } else {
                layout_main.view?.let { parentView ->
                    Snackbar.make(parentView, "Sold ${cryptoAmount.toString().toDouble()} " +
                            "$cryptocurrency for ${String.format("%.2f", totalPrice)}", Snackbar.LENGTH_SHORT).show()
                }
                dialog?.dismiss()
            }
        }

        etSellInputCrypto.addTextChangedListener(inputCryptoTextWatcher(etSellInputCrypto, etSellInputUsd))
        etSellInputUsd.addTextChangedListener(inputUsdTextWatcher(etSellInputUsd, etSellInputCrypto))

        etSellInputCrypto.setOnKeyListener(onEnterClickPerformBuyActionKeyListener())
        etSellInputUsd.setOnKeyListener(onEnterClickPerformBuyActionKeyListener())

        etSellInputCrypto.setText("1")
        etSellInputCrypto.setSelection(1)
        etSellInputUsd.setText(String.format("%.2f", price))
    }

    // need this method to bridge between coroutine scopes and 'normal' code
    private fun getAvailableCryptocurrency() : Double {
        var availableCrypto = 0.0

        runBlocking {
            availableCrypto = getCryptoAmt()
        }

        return availableCrypto
    }

    private suspend fun getCryptoAmt() : Double {
        var total: Double = 0.0

        runBlocking {
            val entry = async { getExistingPortfolioEntry() }

            runBlocking {
                total = entry.await().amount
            }
        }

        return total
    }

    private suspend fun getExistingPortfolioEntry() : PortfolioEntry {
        return portfolioRepository.getPortfolioEntry(cryptocurrency) ?: PortfolioEntry(cryptocurrency, 0.0)
    }

    override fun onEnterPress() {
        btnPopupSell.callOnClick()
    }

    private fun sellCrypto(sellAmount: Double, totalPrice: Double) {
        val balance = preferences.getPreferences().getFloat(KEY_USD_BALANCE, 0f)
        preferences.setPreference(KEY_USD_BALANCE, balance + totalPrice)

        coroutineScope.launch {
            val portfolioEntry = withContext(Dispatchers.IO) {
                portfolioRepository.getPortfolioEntry(cryptocurrency)
                        ?: throw IllegalStateException("trying to sell cryptocurrency that is not in portfolio")
            }

            withContext(Dispatchers.IO) {
                portfolioRepository.insertPortfolioEntry(PortfolioEntry(cryptocurrency, portfolioEntry.amount - sellAmount))

                // add to tradingTransaction table to record all trades
                val tradingTransaction = TradingTransaction(sellAmount, price,
                        cryptocurrency.toString() + FiatCurrency.USD.toString(),
                        Action.SELL, Date())
                tradingTransactionRepository.insertTradingTransaction(tradingTransaction)
            }
        }
    }
}