package com.example.android.architecture.blueprints.todoapp.screen

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.MenuItem
import android.widget.TextView
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.screen.statistics.StatisticsFragment
import com.example.android.architecture.blueprints.todoapp.screen.tasks.TasksFragment
import com.example.android.architecture.blueprints.todoapp.util.replaceFragmentInActivity
import com.naver.android.svc.core.SvcBaseActivity
import com.naver.android.svc.core.SvcBaseFragment

/**
 * @author bs.nam@navercorp.com
 */
class MainActivity : SvcBaseActivity<MainViews, MainCT>() {
    val contentFragment: Fragment
        get() = supportFragmentManager.findFragmentById(R.id.contentFrame)

    override fun createControlTower() = MainCT(this, views)
    override fun createViews() = MainViews(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.findFragmentById(R.id.contentFrame)
                as TasksFragment? ?: TasksFragment().also {
            replaceFragmentInActivity(it, R.id.contentFrame)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            // Open the navigation drawer when the home icon is selected from the toolbar.
            views.openDrawer()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun showStatisticFragment() {
        val currentFragment = contentFragment
        if(currentFragment !is StatisticsFragment){
            views.hideFab()
            replaceFragmentInActivity(StatisticsFragment(), R.id.contentFrame)
        }
    }

    fun showTaskList() {
        val currentFragment = contentFragment
        if(currentFragment !is TasksFragment){
            views.showFab()
            replaceFragmentInActivity(TasksFragment(), R.id.contentFrame)
        }
    }
}