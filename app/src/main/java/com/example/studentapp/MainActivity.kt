package com.example.studentapp

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.studentapp.databinding.ActivityMainBinding
import com.example.studentapp.ui.classes.ClassesFragment
import com.example.studentapp.ui.stopwatch.StopwatchFragment
import com.example.studentapp.ui.stopwatch.insights.InsightsFragment
import com.google.android.material.appbar.MaterialToolbar


// TODO java.lang.IllegalArgumentException: The fragment EventEditFragment{4f8871c} (ebe88115-7f08-407e-b12b-a6eee164fe69 id=0x7f080128) is unknown to the FragmentNavigator. Please use the navigate() function to add fragments to the FragmentNavigator managed FragmentManager.                                                                                        java.lang.IllegalArgumentException: The fragment EventEditFragment{4f8871c} (ebe88115-7f08-407e-b12b-a6eee164fe69 id=0x7f080128) is unknown to the FragmentNavigator. Please use the navigate() function to add fragments to the FragmentNavigator managed FragmentManager.
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SharedData.init(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // bottom navigation
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_classes,
                R.id.navigation_stopwatch,
                R.id.navigation_timetable,
                R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // listener that fires everytime fragment is changed
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                // if we are going to stopwatch or insights hide toolbar
                R.id.navigation_stopwatch, R.id.navigation_insights -> {hideDefaultToolbar() }
                // else show toolbar
                else -> {showDefaultToolbar()}
            }
        }

        // make app follow devices default theme
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }

    // TODO make that this is executed when switching between things in bottom menu
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = currentFocus
        if (view != null) {
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }

        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    fun hideDefaultToolbar() {
        supportActionBar?.hide()
        binding.includedToolbar.toolbarStopwatch.visibility = View.VISIBLE
    }

    fun showDefaultToolbar() {
        supportActionBar?.show()
        binding.includedToolbar.toolbarStopwatch.visibility = View.GONE
    }
}