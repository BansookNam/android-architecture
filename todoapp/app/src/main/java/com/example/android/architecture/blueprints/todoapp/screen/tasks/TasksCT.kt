package com.example.android.architecture.blueprints.todoapp.screen.tasks

import android.app.Activity
import com.example.android.architecture.blueprints.todoapp.Injection
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.TasksDataSource
import com.example.android.architecture.blueprints.todoapp.screen.addedittask.AddEditTaskActivity
import com.example.android.architecture.blueprints.todoapp.tasks.TasksFilterType
import com.example.android.architecture.blueprints.todoapp.util.EspressoIdlingResource
import com.naver.android.svc.core.SvcBaseCT
import java.util.*

/**
 * @author bs.nam@navercorp.com
 */
class TasksCT(owner: TasksFragment, views: TasksViews) : SvcBaseCT<TasksFragment, TasksViews>(owner, views), TasksUseCase {

    var currentFiltering: TasksFilterType = TasksFilterType.ACTIVE_TASKS
    private var firstLoad = true
    private val tasksRepository by lazy { Injection.provideTasksRepository(activity!!.applicationContext) }

    override fun onCreated() {
        loadTasks(false)
    }

    fun result(requestCode: Int, resultCode: Int) {
        // If a task was successfully added, show snackbar
        if (AddEditTaskActivity.REQUEST_ADD_TASK ==
                requestCode && Activity.RESULT_OK == resultCode) {
            views.showSuccessfullySavedMessage()
            loadTasks(false)
        }
    }

    override fun onTaskClick(clickedTask: Task) {
        openTaskDetails(clickedTask)
    }

    override fun onCompleteTaskClick(completedTask: Task) {
        completeTask(completedTask)
    }

    override fun onActivateTaskClick(activatedTask: Task) {
        activateTask(activatedTask)
    }

    override fun onRefresh() {
        loadTasks(false)
    }

    override fun onClickTaskAdd() {
        owner.startEditTaskActivity()
    }

    fun loadTasks(forceUpdate: Boolean) {
        // Simplification for sample: a network reload will be forced on first load.
        loadTasks(forceUpdate || firstLoad, true)
        firstLoad = false

    }

    /**
     * @param forceUpdate   Pass in true to refresh the data in the [TasksDataSource]
     * *
     * @param showLoadingUI Pass in true to display a loading icon in the UI
     */
    private fun loadTasks(forceUpdate: Boolean, showLoadingUI: Boolean) {
        if (showLoadingUI) {
            views.setLoadingIndicator(true)
        }
        if (forceUpdate) {
            tasksRepository.refreshTasks()
        }

        // The network request might be handled in a different thread so make sure Espresso knows
        // that the app is busy until the response is handled.
        EspressoIdlingResource.increment() // App is busy until further notice

        tasksRepository.getTasks(object : TasksDataSource.LoadTasksCallback {
            override fun onTasksLoaded(tasks: List<Task>) {
                val tasksToShow = ArrayList<Task>()

                // This callback may be called twice, once for the cache and once for loading
                // the data from the server API, so we check before decrementing, otherwise
                // it throws "Counter has been corrupted!" exception.
                if (!EspressoIdlingResource.countingIdlingResource.isIdleNow) {
                    EspressoIdlingResource.decrement() // Set app as idle.
                }

                // We filter the tasks based on the requestType
                for (task in tasks) {
                    when (currentFiltering) {
                        TasksFilterType.ALL_TASKS -> tasksToShow.add(task)
                        TasksFilterType.ACTIVE_TASKS -> if (task.isActive) {
                            tasksToShow.add(task)
                        }
                        TasksFilterType.COMPLETED_TASKS -> if (task.isCompleted) {
                            tasksToShow.add(task)
                        }
                    }
                }
                // The view may not be able to handle UI updates anymore
                if (!views.isActive) {
                    return
                }
                if (showLoadingUI) {
                    views.setLoadingIndicator(false)
                }

                processTasks(tasksToShow)
            }

            override fun onDataNotAvailable() {
                // The view may not be able to handle UI updates anymore
                if (!views.isActive) {
                    return
                }
                views.showLoadingTasksError()
            }
        })
    }


    private fun processTasks(tasks: List<Task>) {
        if (tasks.isEmpty()) {
            // Show a message indicating there are no tasks for that filter type.
            processEmptyTasks()
        } else {
            // Show the list of tasks
            views.showTasks(tasks)
            // Set the filter label's text.
            showFilterLabel()
        }
    }

    private fun showFilterLabel() {
        when (currentFiltering) {
            TasksFilterType.ACTIVE_TASKS -> views.showActiveFilterLabel()
            TasksFilterType.COMPLETED_TASKS -> views.showCompletedFilterLabel()
            else -> views.showAllFilterLabel()
        }
    }

    private fun processEmptyTasks() {
        when (currentFiltering) {
            TasksFilterType.ACTIVE_TASKS -> views.showNoActiveTasks()
            TasksFilterType.COMPLETED_TASKS -> views.showNoCompletedTasks()
            else -> views.showNoTasks()
        }
    }

    fun openTaskDetails(requestedTask: Task) {
        owner.startTaskDetailActivity(requestedTask.id)
    }

    fun completeTask(completedTask: Task) {
        tasksRepository.completeTask(completedTask)
        views.showTaskMarkedComplete()
        loadTasks(false, false)
    }

    fun activateTask(activeTask: Task) {
        tasksRepository.activateTask(activeTask)
        views.showTaskMarkedActive()
        loadTasks(false, false)
    }

    fun clearCompletedTasks() {
        tasksRepository.clearCompletedTasks()
        views.showCompletedTasksCleared()
        loadTasks(false, false)
    }

}