package com.example.android.architecture.blueprints.todoapp.screen

import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.view.Gravity
import com.example.android.architecture.blueprints.todoapp.R
import com.naver.android.svc.core.views.UseCaseViews
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

/**
 * @author bs.nam@navercorp.com
 */
class MainViews(screen: MainActivity) : UseCaseViews<MainUseCase>(screen) {

    val drawerLayout by lazy { screen.drawer_layout }
    val toolbar by lazy { screen.toolbar }
    val fab by lazy { screen.fab_add_task }

    override val layoutResId: Int
        get() = R.layout.activity_main

    override fun onCreated() {

        fab.apply {
            setImageResource(R.drawable.ic_add)
            setOnClickListener { useCase.onClickFabTaskAdd() }
        }


        drawerLayout.setStatusBarBackground(R.color.colorPrimaryDark)
        setupDrawerContent(rootView.nav_view)
    }

    fun openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START)
    }

    private fun setupDrawerContent(navigationView: NavigationView) {
        navigationView.setNavigationItemSelectedListener { menuItem ->

            when (menuItem.itemId) {
                R.id.statistics_navigation_menu_item -> useCase.onClickStatisticMenu()
                R.id.list_navigation_menu_item -> useCase.onClickTaskListMenu()
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