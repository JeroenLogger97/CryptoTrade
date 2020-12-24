package com.example.cryptotrade.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.cryptotrade.databinding.FragmentHistoryBinding
import com.example.cryptotrade.util.Constants
import com.example.cryptotrade.vm.HistoryViewModel

class HistoryFragment : Fragment() {

    private val viewModel: HistoryViewModel by viewModels()
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeHistory()
        observeError()

        initViews()

    }

    private fun initViews() {
        _binding?.btnRefresh?.setOnClickListener {
            Log.d(Constants.TAG, "CLICKED REFRESH HISTORY")
            refreshHistory()
        }
    }

    private fun refreshHistory() {
        //todo: use this good solution instead of System.currentTimeMillis()
//        val from: Long = LocalDateTime.now()
//                .minus(7, ChronoUnit.DAYS)
//                .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        val from: Long = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1_000)

        // take a period of 30 minutes, we limit on 5 so we only get 5 back from the API.
        // we do need to make sure there is at least some data retrieved though, so that's why
        // we get data from 30 minutes to make sure there is at least one transaction and price
        val to: Long = from + (30 * 60 * 1_000)

        viewModel.getHistory("BTCUSD", from, to)
    }

    private fun observeHistory() {
        viewModel.history.observe(viewLifecycleOwner, {
            binding.tvHistory.text = it.toString()
        })
    }

    private fun observeError() {
        viewModel.errorText.observe(viewLifecycleOwner, {
            Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
        })
    }
}