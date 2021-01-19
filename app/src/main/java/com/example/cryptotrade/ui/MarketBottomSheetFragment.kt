package com.example.cryptotrade.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import com.example.cryptotrade.model.database.Cryptocurrency
import com.example.cryptotrade.repository.PortfolioRepository
import com.example.cryptotrade.repository.TradingTransactionRepository
import com.example.cryptotrade.util.Preferences
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.util.*

abstract class MarketBottomSheetFragment : BottomSheetDialogFragment() {

    protected lateinit var portfolioRepository: PortfolioRepository
    protected lateinit var tradingTransactionRepository: TradingTransactionRepository
    protected val preferences: Preferences by lazy { Preferences(requireContext()) }
    protected val coroutineScope = CoroutineScope(Dispatchers.Main)

    protected var price: Double = 0.0
    protected lateinit var cryptocurrency: Cryptocurrency

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        portfolioRepository = PortfolioRepository(requireContext())
        tradingTransactionRepository = TradingTransactionRepository(requireContext())

        val symbol = arguments?.getString(SYMBOL_KEY)
                ?: throw IllegalArgumentException("symbol not passed to buy fragment")
        cryptocurrency = Cryptocurrency.fromTradingPair(symbol)

        val lastPrice = arguments?.getDouble(PRICE_KEY)
                ?: throw IllegalArgumentException("price not passed to buy fragment")
        price = lastPrice
    }

    abstract fun onEnterPress()

    protected fun onEnterClickPerformBuyActionKeyListener() : View.OnKeyListener {
        return View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
//                btnPopupBuy.callOnClick()
                onEnterPress()
                return@OnKeyListener true
            }
            false
        }
    }

    protected fun inputCryptoTextWatcher(cryptoEditText: EditText, usdEditText: EditText) : TextWatcher {
        return object : TextWatcher {
            // input type is numberDecimal, so only numbers can be used as input
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // we want the changed text, so use afterTextChanged
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // we want the changed text, so use afterTextChanged
            }

            override fun afterTextChanged(p0: Editable?) {
                if (cryptoEditText.hasFocus() && cryptoEditText.text.toString().isNotEmpty()) {
                    val value = price * cryptoEditText.text.toString().toDouble()

                    usdEditText.setText(String.format(Locale.ENGLISH, "$%.2f", value))
                }
            }
        }
    }

    protected fun inputUsdTextWatcher(usdEditText: EditText, cryptoEditText: EditText) : TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // we want the changed text, so use afterTextChanged
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // we want the changed text, so use afterTextChanged
            }

            override fun afterTextChanged(p0: Editable?) {
                if (!usdEditText.text.toString().startsWith("$")) {
                    usdEditText.setText("$${usdEditText.text}")
                    usdEditText.setSelection(1)
                }

                val inputPrice = usdEditText.text.toString().substring(1)
                if (usdEditText.hasFocus() && inputPrice.isNotEmpty()) {
                    val amountOfCurrency = inputPrice.toDouble() / price
                    cryptoEditText.setText(String.format(Locale.ENGLISH, "%.5f", amountOfCurrency))
                }
            }
        }
    }
}