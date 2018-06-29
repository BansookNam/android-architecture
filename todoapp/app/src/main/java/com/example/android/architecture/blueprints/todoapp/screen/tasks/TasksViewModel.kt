package com.example.android.architecture.blueprints.todoapp.screen.tasks

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.TasksDataSource
import com.example.android.architecture.blueprints.todoapp.data.source.TasksRepository
import com.example.android.architecture.blueprints.todoapp.util.EspressoIdlingResource
import java.util.*

class TasksViewModel : ViewModel() {
    var tasksRepository: TasksRepository? = null

    val tasks = MutableLiveData<MutableList<Task>>()

    fun refreshTasks() {
        tasksRepository?.refreshTasks()
    }

    fun getTasks(currentFiltering: TasksFilterType, callback: TasksDataSource.LoadTasksCallback) {
        tasksRepository?.getTasks(object : TasksDataSource.LoadTasksCallback {
            override fun onTasksLoaded(taskList: List<Task>) {
                val tasksToShow = ArrayList<Task>()

                // This callback may be called twice, once for the cache and once for loading
                // the data from the server API, so we check before decrementing, otherwise
                // it throws "Counter has been corrupted!" exception.
                if (!EspressoIdlingResource.countingIdlingResource.isIdleNow) {
                    EspressoIdlingResource.decrement() // Set app as idle.
                }

                // We filter the tasks based on the requestType
                for (task in taskList) {
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
                tasks.value = tasksToShow
                callback.onTasksLoaded(tasksToShow)
            }

            override fun onDataNotAvailable() {
                callback.onDataNotAvailable()
            }
        })
    }

    fun completeTask(completedTask: Task) {
        tasksRepository?.completeTask(completedTask)
    }

    fun activateTask(activeTask: Task) {
        tasksRepository?.activateTask(activeTask)
    }

    fun clearCompletedTasks() {
        tasksRepository?.clearCompletedTasks()
    }
}