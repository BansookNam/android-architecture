package com.example.android.architecture.blueprints.todoapp.screen

import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.util.setupActionBar
import com.naver.android.svc.core.views.UseCaseViews
import kotlinx.android.synthetic.main.activity_tasks.*

/**
 * @author bs.nam@navercorp.com
 */
class TasksViews(owner: TasksActivity) : UseCaseViews<TasksActivity, TasksUseCase>(owner) {

    val drawerLayout by lazy { owner.drawer_layout }

    override val layoutResId: Int
        get() = R.layout.activity_tasks

    override fun onCreated() {

        owner.setupActionBar(R.id.toolbar) {
            setHomeAsUpIndicator(R.drawable.ic_menu)
            setDisplayHomeAsUpEnabled(true)
        }

        drawerLayout.setStatusBarBackground(R.color.colorPrimaryDark)
        setupDrawerContent(owner.nav_view)
    }

    fun openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START)
    }

    private fun setupDrawerContent(navigationView: NavigationView) {
        navigationView.setNavigationItemSelectedListener { menuItem ->
            if (menuItem.itemId == R.id.statistics_navigation_menu_item) {
                usecase.onClickStatisticMenu()
            }
            // Close the navigation drawer when an item is selected.
            menuItem.isChecked = true
            drawerLayout.closeDrawers()
            true
        }
    }
}