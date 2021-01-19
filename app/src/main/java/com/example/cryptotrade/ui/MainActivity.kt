package com.example.cryptotrade.ui

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.cryptotrade.R
import com.example.cryptotrade.databinding.ActivityMainBinding
import com.example.cryptotrade.util.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder

// todo:
//  - add difficulty in introduction screen: easy no commission, hard high commission (+- 3%)
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val navController: NavController by lazy { findNavController(R.id.layout_main) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setSupportActionBar(binding.toolbar)

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
                showIntroductionDialog()
            }
            else -> {
                return
            }
        }
    }

    private fun getAppStartType(): AppStartType {
        //todo change this while testing!
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

    private fun showIntroductionDialog() {
        StartingBudgetDialogFragment().show(supportFragmentManager, "StartingBudgetDialogFragment")
    }
}

class StartingBudgetDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val singleItems = arrayOf("$1,000", "$10,000", "$100,000")
        val checkedItem = 0
        var selectedIndex = 0

        return activity?.let {
            val builder = MaterialAlertDialogBuilder(it)
            builder.setTitle("Welcome to CryptoTrade! Choose your starting budget:")
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.ok)) { _, _ ->
                        // Respond to positive button press
                        val value = singleItems[selectedIndex].replace(",", "").substring(1).toDouble()

                        Preferences(it).setPreference(KEY_USD_BALANCE, value)
                    }
                    // Single-choice items (initialized with checked item)
                    .setSingleChoiceItems(singleItems, checkedItem) { _, which ->
                        // Respond to item chosen
                        selectedIndex = which
                    }
                    .show()

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}