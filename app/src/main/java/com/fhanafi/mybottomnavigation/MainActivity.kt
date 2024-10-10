package com.fhanafi.mybottomnavigation

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.fhanafi.mybottomnavigation.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_upcoming, R.id.navigation_notifications, R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Set ActionBar title based on the current destination
        navController.addOnDestinationChangedListener { _, destination, _ ->
            setActionBarTitle(getTitleForDestination(destination.id))
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
            R.id.navigation_profile -> "Profile"
            else -> "My App" // Default title
        }
        Log.d("MainActivity", "Current Fragment ID: $destinationId, Title: $title")
        return title
    }
}