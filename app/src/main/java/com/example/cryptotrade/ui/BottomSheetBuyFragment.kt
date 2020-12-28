package com.example.cryptotrade.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cryptotrade.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_buy.*

const val SYMBOL_KEY = "buy_symbol_key"

class BottomSheetBuyFragment : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_buy, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val symbol = arguments?.getString(SYMBOL_KEY)
        tvBuyCryptocurrency.text = symbol?.substring(0, 3)

        btnPopupBuy.setOnClickListener {
            dialog?.dismiss()
        }
        etBuyInputCrypto.setText("1")
        etBuyInputUsd.setText("$10.010,51")
    }

}