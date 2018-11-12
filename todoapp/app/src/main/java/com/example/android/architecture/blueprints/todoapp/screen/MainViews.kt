package com.example.android.architecture.blueprints.todoapp.screen

import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.view.Gravity
import com.example.android.architecture.blueprints.todoapp.R
import com.naver.android.svc.core.views.ActionViews
import kotlinx.android.synthetic.main.activity_main.view.*

/**
 * @author bs.nam@navercorp.com
 */
class MainViews : ActionViews<MainViewsAction>() {

    private val drawerLayout by lazy { rootView.drawer_layout }
    private val fab by lazy { rootView.fab_add_task }

    override val layoutResId: Int
        get() = R.layout.activity_main

    override fun onCreated() {
        fab.apply {
            setImageResource(R.drawable.ic_add)
            setOnClickListener { viewsAction.onClickFabTaskAdd() }
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
                R.id.statistics_navigation_menu_item -> viewsAction.onClickStatisticMenu()
                R.id.list_navigation_menu_item -> viewsAction.onClickTaskListMenu()
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