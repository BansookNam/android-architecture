package com.example.android.architecture.blueprints.todoapp.screen

import android.support.annotation.StringRes
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.view.Gravity
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.util.setupActionBar
import com.naver.android.svc.core.views.UseCaseViews
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @author bs.nam@navercorp.com
 */
class MainViews(owner: MainActivity) : UseCaseViews<MainActivity, MainUseCase>(owner) {

    val drawerLayout by lazy { owner.drawer_layout }
    val toolbar by lazy { owner.toolbar }
    val fab by lazy { owner.fab_add_task }

    override val layoutResId: Int
        get() = R.layout.activity_main

    override fun onCreated() {

        owner.setupActionBar(R.id.toolbar) {
            setHomeAsUpIndicator(R.drawable.ic_menu)
            setDisplayHomeAsUpEnabled(true)
        }

        fab.apply {
            setImageResource(R.drawable.ic_add)
            setOnClickListener { usecase.onClickFabTaskAdd() }
        }


        drawerLayout.setStatusBarBackground(R.color.colorPrimaryDark)
        setupDrawerContent(owner.nav_view)
    }

    fun setTitle(@StringRes title: Int) {
        owner.supportActionBar?.setTitle(title)
    }

    fun openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START)
    }

    private fun setupDrawerContent(navigationView: NavigationView) {
        navigationView.setNavigationItemSelectedListener { menuItem ->

            when (menuItem.itemId) {
                R.id.statistics_navigation_menu_item -> usecase.onClickStatisticMenu()
                R.id.list_navigation_menu_item -> usecase.onClickTaskListMenu()
            }
            // Close the navigation drawer when an item is selected.
            menuItem.isChecked = true
            drawerLayout.closeDrawers()
            true
        }
    }

    fun hideFab() {
        fab.hide()
    }

    fun showFab() {
        fab.show()
    }

    override fun onBackPressed(): Boolean {
        return if (drawerLayout.isDrawerOpen(Gravity.START)) {
            drawerLayout.closeDrawers()
            true
        } else {
            super.onBackPressed()
        }
    }
}