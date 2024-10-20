package com.fhanafi.mybottomnavigation

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.fhanafi.mybottomnavigation.databinding.ActivityMainBinding
import com.fhanafi.mybottomnavigation.ui.setting.SettingPreferences
import com.fhanafi.mybottomnavigation.ui.setting.SettingViewModel
import com.fhanafi.mybottomnavigation.ui.setting.SettingsViewModelFactory
import com.fhanafi.mybottomnavigation.ui.setting.dataStore

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_upcoming, R.id.navigation_notifications,R.id.navigation_favorite ,R.id.navigation_setting
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            setActionBarTitle(getTitleForDestination(destination.id))
        }

        // Mengambil pengaturan tema dan mengimplementasikan saat startup
        val pref = SettingPreferences.getInstance(dataStore)
        val settingViewModel = ViewModelProvider(this, SettingsViewModelFactory(pref))[SettingViewModel::class.java]

        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive ->
            AppCompatDelegate.setDefaultNightMode(
                if (isDarkModeActive) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )
        }
    }

    private fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }

    private fun getTitleForDestination(destinationId: Int): String {
        val title = when (destinationId) {
            R.id.navigation_home -> "Home"
            R.id.navigation_upcoming -> "Upcoming Events"
            R.id.navigation_notifications -> "Finished Events"
            R.id.navigation_favorite -> "Favorite"
            R.id.navigation_setting -> "Setting"
            else -> "My App"
        }
        return title
    }
}
