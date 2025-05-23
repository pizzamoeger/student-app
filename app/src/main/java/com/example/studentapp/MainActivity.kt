package com.example.studentapp

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.studentapp.databinding.ActivityMainBinding
import com.example.studentapp.ui.timetable.TimetableWidget

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        SharedData.init(this)
        refreshWidgets(this)

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
                R.id.navigation_assignments
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

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

    fun refreshWidgets(context: Context) {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val thisWidget = ComponentName(context, TimetableWidget::class.java)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget)

        // Call your widget's onUpdate manually for all widget instances
        if (appWidgetIds.isNotEmpty()) {
            TimetableWidget().onUpdate(context, appWidgetManager, appWidgetIds)
        }
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
            navigateToFragment(R.id.fragment_semester)
            popupMenu.visibility = View.GONE
        }
        fragment2Button.setOnClickListener {
            navigateToFragment(R.id.fragment_grades)
            popupMenu.visibility = View.GONE
        }
        fragment3Button.setOnClickListener {
            navigateToFragment(R.id.fragment_authentification)
            popupMenu.visibility = View.GONE
        }
    }

    private fun navigateToFragment(destinationId: Int) {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        //navController.popBackStack()
        navController.navigate(destinationId)
        binding.popupMenu.popupMenu.visibility = View.GONE
    }
}