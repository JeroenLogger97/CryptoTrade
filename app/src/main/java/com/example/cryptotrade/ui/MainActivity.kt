package com.example.cryptotrade.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.cryptotrade.R
import com.example.cryptotrade.databinding.ActivityMainBinding
import com.example.cryptotrade.util.*
import com.example.cryptotrade.vm.TickerViewModel

// todo:
//  - add start popup screen where you can choose your starting money (100, 1000, 10000)
//  - add difficulty in introduction screen: easy no commission, hard high commission (+- 3%)
class MainActivity : AppCompatActivity() {

    private val viewModel: TickerViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private val navController: NavController by lazy { findNavController(R.id.nav_host_fragment) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setSupportActionBar(binding.toolbar)

        binding.fab.setOnClickListener {
            // todo what to use the fab for?
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24)
        supportActionBar?.title = "CryptoTrade"

        binding.toolbar.setNavigationOnClickListener {
            Log.d(Constants.TAG, "CLICKED MENU")
        }

        handleAppStart()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_market -> {
                navController.navigate(R.id.marketFragment)
                true
            }
            R.id.action_portfolio -> {
                navController.navigate(R.id.portfolioFragment)
                true
            }
            R.id.action_history -> {
                navController.navigate(R.id.historyFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun handleAppStart() {
        when (getAppStartType()) {
            AppStartType.FIRST_TIME -> {
                // first ever run: show introduction dialog
//                showIntroductionDialog()
                Preferences(applicationContext).setPreference(KEY_USD_BALANCE, 10_000)
            }
            else -> {
                return
            }
        }
    }

    private fun getAppStartType() : AppStartType {
        if (1 == 1) {
            return AppStartType.FIRST_TIME
        }

        val preferences = Preferences(applicationContext)

        val lastVersion = preferences.getPreferences().getInt(KEY_LAST_VERSION_RUN, DEFAULT_LAST_VERSION_RUN)

        val currentVersion = packageManager?.getPackageInfo(packageName, 0)?.versionCode ?: 0
        preferences.setPreference(KEY_LAST_VERSION_RUN, currentVersion)

        return when (lastVersion) {
            DEFAULT_LAST_VERSION_RUN -> {
                AppStartType.FIRST_TIME
            }
            else -> {
                AppStartType.NORMAL
            }
        }
    }

    //todo remove method
    private fun showIntroductionDialog() {
//        val intent = Intent(this, IntroductionFragment::class.java)
//        intent.flags = Intent.FLAG_RECEIVER_FOREGROUND
//        startActivity(intent)
//
//        findNavController(R.id.nav_host_fragment).navigate(R.id.introductionFragment)
    }
}