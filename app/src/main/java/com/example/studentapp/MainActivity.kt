package com.example.studentapp

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.studentapp.databinding.ActivityMainBinding
import com.example.studentapp.ui.classes.ClassesFragment
import com.example.studentapp.ui.stopwatch.StopwatchFragment
import com.google.android.material.appbar.MaterialToolbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SharedData.init(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // toolbar
        setSupportActionBar(findViewById(R.id.toolbar))

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
    }

    // TODO make that this is executed when switching between things in bottom menu
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    fun hideDefaultToolbar() {
        supportActionBar?.hide()
    }

    fun showDefaultToolbar() {
        //setSupportActionBar(findViewById(R.id.toolbar))
        /*supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            this.title =
        }*/
        supportActionBar?.show()
    }

}