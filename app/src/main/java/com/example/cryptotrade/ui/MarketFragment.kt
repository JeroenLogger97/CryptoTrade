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
import com.example.cryptotrade.repository.BitfinexRepository
import com.example.cryptotrade.vm.TickerViewModel
import kotlinx.coroutines.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MarketFragment : Fragment() {

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
//        return inflater.inflate(R.layout.fragment_market, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeTicker()

        _binding?.btnRefresh?.setOnClickListener {
            Log.d("TAG", "clicked refresh! refreshing....")
            viewModel.getTicker("tBTCEUR")
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
}