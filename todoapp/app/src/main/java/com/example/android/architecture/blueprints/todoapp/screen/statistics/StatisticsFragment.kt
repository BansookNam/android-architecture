package com.example.android.architecture.blueprints.todoapp.screen.statistics

import com.example.android.architecture.blueprints.todoapp.Injection
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.screen.base.BaseFragment

/**
 * @author bs.nam@navercorp.com
 */
class StatisticsFragment : BaseFragment<StatisticsViews, StatisticsControlTower>() {

    override val fragmentTitleResId: Int
        get() = R.string.statistics_title

    override fun createControlTower() = StatisticsControlTower(this, views, Injection.provideTasksRepository(activity!!.applicationContext))
    override fun createViews() = StatisticsViews()

}