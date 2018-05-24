package com.example.android.architecture.blueprints.todoapp.screen.statistics

import android.os.Bundle
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.screen.MainActivity
import com.naver.android.svc.core.SvcBaseFragment

/**
 * @author bs.nam@navercorp.com
 */
class StatisticsFragment : SvcBaseFragment<StatisticsViews, StatisticsCT>() {


    override fun createControlTower() = StatisticsCT(this, views)
    override fun createViews() = StatisticsViews(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).supportActionBar?.setTitle(R.string.statistics_title)
    }
}