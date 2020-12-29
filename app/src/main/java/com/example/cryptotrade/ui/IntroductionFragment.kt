package com.example.cryptotrade.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.cryptotrade.util.KEY_USD_BALANCE
import com.example.cryptotrade.util.Preferences
import kotlinx.android.synthetic.main.fragment_introduction.*

//todo remove this file: not used
class IntroductionFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {
        btnOptionA.setOnClickListener {
            setBalance(100.0)
            activity?.supportFragmentManager?.popBackStack()
        }

        btnOptionB.setOnClickListener {
            setBalance(1000.0)
            activity?.supportFragmentManager?.popBackStack()
        }

        btnOptionC.setOnClickListener {
            setBalance(10000.0)
            activity?.supportFragmentManager?.popBackStack()
        }
    }

    private fun setBalance(balance: Double) {
        val preferences = Preferences(requireContext())

        preferences.setPreference(KEY_USD_BALANCE, balance)
    }
}