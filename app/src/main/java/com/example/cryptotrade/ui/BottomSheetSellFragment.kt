package com.example.cryptotrade.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cryptotrade.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_sell.*

class BottomSheetSellFragment : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_sell, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val symbol = arguments?.getString(SYMBOL_KEY)
        tvSellCryptocurrency.text = symbol?.substring(0, 3)

        btnPopupSell.setOnClickListener {
            dialog?.dismiss()
        }
        etSellInputCrypto.setText("1")
        etSellInputUsd.setText("$10.010,51")
    }
}