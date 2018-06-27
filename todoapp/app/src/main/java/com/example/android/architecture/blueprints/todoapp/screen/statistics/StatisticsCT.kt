package com.example.android.architecture.blueprints.todoapp.screen.statistics

import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.TasksDataSource
import com.example.android.architecture.blueprints.todoapp.util.EspressoIdlingResource
import com.naver.android.svc.core.controltower.SvcCT

/**
 * @author bs.nam@navercorp.com
 */
class StatisticsCT(screen: StatisticsFragment, views: StatisticsViews, private var tasksRepository: TasksDataSource) : SvcCT<StatisticsFragment, StatisticsViews>(screen, views) {

    override fun onCreated() {
        loadStatistics()
    }

    private fun loadStatistics() {
        views.setProgressIndicator(true)

        // The network request might be handled in a different thread so make sure Espresso knows
        // that the app is busy until the response is handled.
        EspressoIdlingResource.increment() // App is busy until further notice

        tasksRepository.getTasks(object : TasksDataSource.LoadTasksCallback {
            override fun onTasksLoaded(tasks: List<Task>) {
                // We calculate number of active and completed tasks
                val completedTasks = tasks.filter { it.isCompleted }.size
                val activeTasks = tasks.size - completedTasks

                // This callback may be called twice, once for the cache and once for loading
                // the data from the server API, so we check before decrementing, otherwise
                // it throws "Counter has been corrupted!" exception.
                if (!EspressoIdlingResource.countingIdlingResource.isIdleNow) {
                    EspressoIdlingResource.decrement() // Set app as idle.
                }
                // The view may not be able to handle UI updates anymore
                if (!screen.isActive) {
                    return
                }
                views.setProgressIndicator(false)
                views.showStatistics(activeTasks, completedTasks)
            }

            override fun onDataNotAvailable() {
                // The view may not be able to handle UI updates anymore
                if (!screen.isActive) {
                    return
                }
                views.showLoadingStatisticsError()
            }
        })
    }
}