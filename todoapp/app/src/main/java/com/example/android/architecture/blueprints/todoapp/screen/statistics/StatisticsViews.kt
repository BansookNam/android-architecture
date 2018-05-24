package com.example.android.architecture.blueprints.todoapp.screen.statistics

import com.example.android.architecture.blueprints.todoapp.R
import com.naver.android.svc.core.views.SvcBaseViews
import kotlinx.android.synthetic.main.fragment_statistics.*

/**
 * @author bs.nam@navercorp.com
 */
class StatisticsViews(owner: StatisticsFragment) : SvcBaseViews<StatisticsFragment>(owner) {

    val statisticsTV by lazy { owner.statistics }

    override val layoutResId: Int
        get() = R.layout.fragment_statistics

    override fun onCreated() {
    }

    fun setProgressIndicator(active: Boolean) {
        if (active) {
            statisticsTV.text = getString(R.string.loading)
        } else {
            statisticsTV.text = ""
        }
    }

    fun showStatistics(numberOfIncompleteTasks: Int, numberOfCompletedTasks: Int) {
        if (numberOfCompletedTasks == 0 && numberOfIncompleteTasks == 0) {
            statisticsTV.text = getString(R.string.statistics_no_tasks)
        } else {
            val displayString = "${getString(R.string.statistics_active_tasks)} " +
                    "$numberOfIncompleteTasks\n" +
                    "${getString(R.string.statistics_completed_tasks)} " +
                    "$numberOfCompletedTasks"
            statisticsTV.text = displayString
        }
    }

    fun showLoadingStatisticsError() {
        statisticsTV.text = getString(R.string.statistics_error)
    }

}