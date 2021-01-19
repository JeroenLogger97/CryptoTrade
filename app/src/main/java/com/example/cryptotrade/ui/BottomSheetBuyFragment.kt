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
import kotlinx.android.synthetic.main.bottom_sheet_buy.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

const val SYMBOL_KEY = "symbol_key" // example value: BTCUSD
const val PRICE_KEY = "price_key"

class BottomSheetBuyFragment : MarketBottomSheetFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_buy, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvBuyCryptocurrency.text = cryptocurrency.toString()
        btnPopupBuy.setOnClickListener {
            var error = ""

            val cryptoAmount = etBuyInputCrypto.text
            val totalPrice = etBuyInputUsd.text.substring(1).toDouble()
            val balance = preferences.getPreferences().getFloat(KEY_USD_BALANCE, 0f)

            when {
                cryptoAmount.isEmpty() -> {
                    error = "Cryptocurrency amount is empty"
                }
                totalPrice == 0.0 -> {
                    error = "Purchase too small"
                }
                balance < totalPrice -> {
                    // not enough balance to buy
                    error = "Not enough balance"
                }
                else -> {
                    purchaseCrypto(cryptoAmount.toString().toDouble(), totalPrice)
                }
            }

            if (error.isNotEmpty()) {
                layout_main.view?.let { parentView ->
                    Snackbar.make(parentView, error, Snackbar.LENGTH_SHORT).show()
                }
            } else {
                layout_main.view?.let { parentView ->
                    Snackbar.make(parentView, "Purchased ${cryptoAmount.toString().toDouble()} " +
                            "$cryptocurrency for ${String.format(Locale.ENGLISH, "%.2f", totalPrice)}", Snackbar.LENGTH_SHORT).show()
                }
                dialog?.dismiss()
            }

        }

        etBuyInputCrypto.addTextChangedListener(inputCryptoTextWatcher(etBuyInputCrypto, etBuyInputUsd))
        etBuyInputUsd.addTextChangedListener(inputUsdTextWatcher(etBuyInputUsd, etBuyInputCrypto))

        etBuyInputCrypto.setOnKeyListener(onEnterClickPerformBuyActionKeyListener())
        etBuyInputUsd.setOnKeyListener(onEnterClickPerformBuyActionKeyListener())

        etBuyInputCrypto.setText("1")
        etBuyInputCrypto.setSelection(1)
        etBuyInputUsd.setText(String.format(Locale.ENGLISH, "%.2f", price))
    }

    override fun onEnterPress() {
        btnPopupBuy.callOnClick()
    }

    private fun purchaseCrypto(buyAmount: Double, totalPrice: Double) {
        val balance = preferences.getPreferences().getFloat(KEY_USD_BALANCE, 0f)
        preferences.setPreference(KEY_USD_BALANCE, balance - totalPrice)

        coroutineScope.launch {
            val portfolioEntry = withContext(Dispatchers.IO) {
                portfolioRepository.getPortfolioEntry(cryptocurrency) ?: PortfolioEntry(cryptocurrency, 0.0)
            }

            withContext(Dispatchers.IO) {
                portfolioRepository.insertPortfolioEntry(PortfolioEntry(cryptocurrency, portfolioEntry.amount + buyAmount))

                // add to tradingTransaction table to record all trades
                val tradingTransaction = TradingTransaction(buyAmount, price,
                        cryptocurrency.toString() + FiatCurrency.USD.toString(),
                        Action.BUY, Date())
                tradingTransactionRepository.insertTradingTransaction(tradingTransaction)
            }
        }
    }

}