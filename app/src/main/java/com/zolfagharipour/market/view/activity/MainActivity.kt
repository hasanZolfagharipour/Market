package com.zolfagharipour.market.view.activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.zolfagharipour.market.R
import com.zolfagharipour.market.databinding.ActivityMainBinding
import com.zolfagharipour.market.other.ShowToast
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private var exit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setupNavigation()
        controlNavigationVisibility()
    }

    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment
        navController = navHostFragment.findNavController()
        binding.bottomNavigationView.setupWithNavController(navController)
        }

    private fun controlNavigationVisibility() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.homeFragment ||
                destination.id == R.id.categoryFragment ||
                destination.id == R.id.basketFragment ||
                destination.id == R.id.myMarketFragment) {
                binding.bottomNavigationView.visibility = View.VISIBLE
            }
            else {
                binding.bottomNavigationView.visibility = View.GONE
                //for handling back on bottomNavigationView HomeFragment should be start destination
                navController.graph.startDestination = R.id.homeFragment
            }

            if (destination.id != R.id.splashFragment){



                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.statusBarColor = Color.WHITE
                }
                // change text status bar text color to visible.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                }
            }

        }
    }

    override fun onBackPressed() {
        if (navController.graph.startDestination == navController.currentDestination?.id) {
            lifecycleScope.launch(IO) {
                delay(2000)
                exit = false
            }
            if (exit)
                super.onBackPressed()
            exit = true
            ShowToast(this, R.string.on_back_message)
        }else
            super.onBackPressed()
    }
}