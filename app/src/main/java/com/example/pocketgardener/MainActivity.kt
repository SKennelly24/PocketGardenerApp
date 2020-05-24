package com.example.pocketgardener

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.IdRes
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private lateinit var viewPager: ViewPager
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var mainPagerAdapter: MainPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize components/views.
        viewPager = findViewById(R.id.view_pager)
        bottomNavigationView = findViewById(R.id.bottom_navigation_view)
        mainPagerAdapter = MainPagerAdapter(supportFragmentManager)

        // Set items to be displayed.
        mainPagerAdapter.setItems(arrayListOf(MainScreen.GARDEN, MainScreen.ADVICE))

        // Show the default screen.
        val defaultScreen = MainScreen.GARDEN
        scrollToScreen(defaultScreen)
        selectBottomNavigationViewMenuItem(defaultScreen.menuItemId)

        // Set the listener for item selection in the bottom navigation view.
        bottomNavigationView.setOnNavigationItemSelectedListener(this)

        // Attach an adapter to the view pager and make it select the bottom navigation
        // menu item and change the title to proper values when selected.
        viewPager.adapter = mainPagerAdapter
        viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                val selectedScreen = mainPagerAdapter.getItems()[position]
                selectBottomNavigationViewMenuItem(selectedScreen.menuItemId)
            }
        })
    }

    /**
     * Scrolls `ViewPager` to show the provided screen.
     */
    private fun scrollToScreen(mainScreen: MainScreen) {
        val screenPosition = mainPagerAdapter.getItems().indexOf(mainScreen)
        if (screenPosition != viewPager.currentItem) {
            viewPager.currentItem = screenPosition
        }
    }

    /**
     * Selects the specified item in the bottom navigation view.
     */
    private fun selectBottomNavigationViewMenuItem(@IdRes menuItemId: Int) {
        bottomNavigationView.setOnNavigationItemSelectedListener(null)
        bottomNavigationView.selectedItemId = menuItemId
        bottomNavigationView.setOnNavigationItemSelectedListener(this)
    }

    /**
     * Listener implementation for registering bottom navigation clicks.
     */
    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        getMainScreenForMenuItem(menuItem.itemId)?.let {
            scrollToScreen(it)
            supportActionBar?.setTitle(it.titleStringId)
            return true
        }
        return false
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.setting_action_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.setting_button -> {
            startActivity(Intent(this, SettingsActivity::class.java))
            true
        }
        else -> super.onOptionsItemSelected(item)
    }



}