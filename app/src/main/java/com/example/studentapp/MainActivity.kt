package com.example.studentapp

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.PopupWindow
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.studentapp.databinding.ActivityMainBinding
import com.example.studentapp.ui.classes.ClassesFragment
import com.example.studentapp.ui.getThemeColor
import com.example.studentapp.ui.stopwatch.StopwatchFragment
import com.example.studentapp.ui.stopwatch.insights.InsightsFragment
import com.github.dhaval2404.colorpicker.util.setVisibility
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
        /*navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                // if we are going to stopwatch or insights hide toolbar
                R.id.navigation_stopwatch, R.id.navigation_insights, R.id.navigation_timetable -> {hideDefaultToolbar(destination.id) }
                // else show toolbar
                else -> {showDefaultToolbar()}
            }
        }*/

        // make app follow devices default theme
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        // if we are locked, backpress should be disabled
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (SharedData.locked) {
                    // disables backpress
                    Toast.makeText(this@MainActivity, "Back button is disabled in this mode", Toast.LENGTH_SHORT).show()
                } else {
                    // enables backpress
                    onBackPressed()
                }
            }
        }

        // when back is pressed execute our callback
        onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        // hide keyboard again so that layout updated accordingly
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = currentFocus
        if (view != null) {
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }

        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun showDefaultToolbar() {
        supportActionBar?.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // create custom menu
        menuInflater.inflate(R.menu.menu_top, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_navigation -> {
                // when we click opions thingy in menu display custom menu (or hide it again)
                val toolbar = findViewById<Toolbar>(R.id.toolbar)
                showFragmentSelectionMenu(toolbar)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun showFragmentSelectionMenu(view: View) {
        // get popup
        val popupMenu = binding.popupMenu.popupMenu

        // if it is already visible, make it invisible and return
        if (popupMenu.visibility == View.VISIBLE) {
            popupMenu.visibility = View.GONE
            return
        }

        // set width and height
        val params = popupMenu.layoutParams
        params.width = resources.displayMetrics.widthPixels/2
        params.height = resources.displayMetrics.heightPixels-binding.toolbar.height
        popupMenu.layoutParams = params

        // make it visible and move it to front
        popupMenu.visibility = View.VISIBLE
        popupMenu.bringToFront()

        // TODO item clicks
        val fragment1Button = binding.popupMenu.fragment1Button
        val fragment2Button = binding.popupMenu.fragment2Button
        val fragment3Button = binding.popupMenu.fragment3Button

        fragment1Button.setOnClickListener {
            navigateToFragment(R.id.navigation_stopwatch)
            popupMenu.visibility = View.GONE
        }
        fragment2Button.setOnClickListener {
            navigateToFragment(R.id.navigation_stopwatch)
            popupMenu.visibility = View.GONE
        }
        fragment3Button.setOnClickListener {
            navigateToFragment(R.id.navigation_stopwatch)
            popupMenu.visibility = View.GONE
        }
    }

    private fun navigateToFragment(destinationId: Int) {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navController.popBackStack()
        navController.navigate(destinationId)
        binding.popupMenu.popupMenu.visibility = View.GONE
    }
}