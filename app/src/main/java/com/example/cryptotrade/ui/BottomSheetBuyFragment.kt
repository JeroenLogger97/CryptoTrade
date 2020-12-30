package com.example.cryptotrade.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.cryptotrade.R
import com.example.cryptotrade.model.database.*
import com.example.cryptotrade.repository.PortfolioRepository
import com.example.cryptotrade.repository.TradingTransactionRepository
import com.example.cryptotrade.util.Constants
import com.example.cryptotrade.util.KEY_USD_BALANCE
import com.example.cryptotrade.util.Preferences
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.bottom_sheet_buy.*
import kotlinx.coroutines.*
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.util.*

const val SYMBOL_KEY = "symbol_key" // example value: BTCUSD
const val PRICE_KEY = "price_key"

class BottomSheetBuyFragment : BottomSheetDialogFragment() {

    private lateinit var portfolioRepository: PortfolioRepository
    private lateinit var tradingTransactionRepository: TradingTransactionRepository
    private val preferences: Preferences by lazy { Preferences(requireContext()) }
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    private var price: Double = 0.0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_buy, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        portfolioRepository = PortfolioRepository(requireContext())
        tradingTransactionRepository = TradingTransactionRepository(requireContext())

        val symbol = arguments?.getString(SYMBOL_KEY)
                ?: throw IllegalArgumentException("symbol not passed to buy fragment")

        val cryptocurrency = Cryptocurrency.fromTradingPair(symbol)
        tvBuyCryptocurrency.text = cryptocurrency.toString()

        val lastPrice = arguments?.getDouble(PRICE_KEY)
                ?: throw IllegalArgumentException("price not passed to buy fragment")
        price = lastPrice

        btnPopupBuy.setOnClickListener {
            var error = ""

            val cryptoAmount = etBuyInputCrypto.text
            val price = etBuyInputUsd.text.substring(1).toDouble()
            val balance = preferences.getPreferences().getFloat(KEY_USD_BALANCE, 0f)

            if (cryptoAmount.isEmpty()) {
                error = "Cryptocurrency amount is empty"
            } else if (price == 0.0) {
                error = "Purchase too small"
            } else if (balance < price) {
                // not enough balance to buy
                error = "Not enough balance"
            } else {
                purchaseCrypto(cryptocurrency, cryptoAmount.toString().toDouble(), price)
            }

            if (error.isNotEmpty()) {
                Toast.makeText(activity, error, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, "Purchased ${cryptoAmount.toString().toDouble()} $cryptocurrency for $price", Toast.LENGTH_SHORT).show()
                dialog?.dismiss()
            }

        }

        etBuyInputCrypto.addTextChangedListener(object : TextWatcher {
            // input type is numberDecimal, so only numbers can be used as input
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // we want the changed text, so use afterTextChanged
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // we want the changed text, so use afterTextChanged
            }

            override fun afterTextChanged(p0: Editable?) {
                if (etBuyInputCrypto.hasFocus() && etBuyInputCrypto.text.toString().isNotEmpty()) {
                    val value = price * etBuyInputCrypto.text.toString().toDouble()

                    etBuyInputUsd.setText(String.format("$%.2f", value))
                }
            }
        })
        etBuyInputUsd.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // we want the changed text, so use afterTextChanged
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // we want the changed text, so use afterTextChanged
            }

            override fun afterTextChanged(p0: Editable?) {
                if (!etBuyInputUsd.text.toString().startsWith("$")) {
                    etBuyInputUsd.setText("$${etBuyInputUsd.text}")
                    etBuyInputUsd.setSelection(1)
                }

                val inputPrice = etBuyInputUsd.text.toString().substring(1)
                if (etBuyInputUsd.hasFocus() && inputPrice.isNotEmpty()) {
                    val amountOfCurrency = inputPrice.toDouble() / price
                    etBuyInputCrypto.setText(String.format("%.5f", amountOfCurrency))
                }
            }
        })

        etBuyInputCrypto.setOnKeyListener(onEnterClickPerformBuyActionKeyListener())
        etBuyInputUsd.setOnKeyListener(onEnterClickPerformBuyActionKeyListener())

        etBuyInputCrypto.setText("1")
        etBuyInputCrypto.setSelection(1)
        etBuyInputUsd.setText(price.toString())
    }

    private fun onEnterClickPerformBuyActionKeyListener() : View.OnKeyListener {
        return View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                btnPopupBuy.callOnClick()
                return@OnKeyListener true
            }
            false
        }
    }

    private fun purchaseCrypto(cryptocurrency: Cryptocurrency, buyAmount: Double, totalPrice: Double) {
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