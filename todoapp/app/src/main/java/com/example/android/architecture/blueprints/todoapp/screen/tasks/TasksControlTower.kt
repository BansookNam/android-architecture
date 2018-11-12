package com.example.android.architecture.blueprints.todoapp.screen.tasks

import android.app.Activity
import android.arch.lifecycle.Observer
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.TasksDataSource
import com.example.android.architecture.blueprints.todoapp.data.source.TasksRepository
import com.example.android.architecture.blueprints.todoapp.util.EspressoIdlingResource
import com.naver.android.svc.core.controltower.ControlTower

/**
 * @author bs.nam@navercorp.com
 */
class TasksControlTower(screen: TasksFragment, views: TasksViews, private val vm: TasksViewModel, val tasksRepository: TasksRepository)
    : ControlTower<TasksFragment, TasksViews>(screen, views), TasksViewsAction {

    var currentFiltering: TasksFilterType = TasksFilterType.ACTIVE_TASKS
    private var firstLoad = true

    override fun onCreated() {
        vm.tasksRepository = tasksRepository
        loadTasks(true)
        vm.tasks.observe(screen, Observer<MutableList<Task>> { task ->
            task ?: return@Observer
            views.setLoadingIndicator(false)
            processTasks(task)
        })
    }

    fun result(requestCode: Int, resultCode: Int) {
        // If a task was successfully added, show snackbar
        when (requestCode) {
            TasksFragment.REQUEST_ADD_TASK -> {
                if (Activity.RESULT_OK == resultCode) {
                    views.showSuccessfullySavedMessage()
                    loadTasks(false)
                }
            }
            TasksFragment.REQUEST_TASK_DETAIL -> loadTasks(false)
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
        screen.startEditTaskActivity()
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
            vm.refreshTasks()
        }

        // The network request might be handled in a different thread so make sure Espresso knows
        // that the app is busy until the response is handled.
        EspressoIdlingResource.increment() // App is busy until further notice

        vm.getTasks(currentFiltering, object : TasksDataSource.LoadTasksCallback {
            override fun onTasksLoaded(tasks: List<Task>) {}

            override fun onDataNotAvailable() {
                if (!screen.isActive) {
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
        screen.startTaskDetailActivity(requestedTask.id)
    }

    fun completeTask(completedTask: Task) {
        vm.completeTask(completedTask)
        views.showTaskMarkedComplete()
        loadTasks(false, false)
    }

    fun activateTask(activeTask: Task) {
        vm.activateTask(activeTask)
        views.showTaskMarkedActive()
        loadTasks(false, false)
    }

    fun clearCompletedTasks() {
        vm.clearCompletedTasks()
        views.showCompletedTasksCleared()
        loadTasks(false, false)
    }
}