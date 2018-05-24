package com.example.android.architecture.blueprints.todoapp.screen.taskdetail

import com.example.android.architecture.blueprints.todoapp.Injection
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.TasksDataSource
import com.naver.android.svc.core.SvcBaseCT

/**
 * @author bs.nam@navercorp.com
 */
class TaskDetailCT(owner: TaskDetailActivity, views: TaskDetailViews, val taskId: String) : SvcBaseCT<TaskDetailActivity, TaskDetailViews>(owner, views), TaskDetailUseCase {

    val tasksRepository: TasksDataSource by lazy { Injection.provideTasksRepository(activity!!.applicationContext) }

    override fun onCreated() {
        openTask()
    }

    private fun openTask() {
        if (taskId.isEmpty()) {
            views.showMissingTask()
            return
        }

        views.setLoadingIndicator(true)
        tasksRepository.getTask(taskId, object : TasksDataSource.GetTaskCallback {
            override fun onTaskLoaded(task: Task) {
                with(views) {
                    // The view may not be able to handle UI updates anymore
                    if (!owner.isAvailable()) {
                        return@onTaskLoaded
                    }
                    setLoadingIndicator(false)
                }
                showTask(task)
            }

            override fun onDataNotAvailable() {
                with(views) {
                    // The view may not be able to handle UI updates anymore
                    if (!owner.isAvailable()) {
                        return@onDataNotAvailable
                    }
                    showMissingTask()
                }
            }
        })
    }

    fun editTask() {
        if (taskId.isEmpty()) {
            views.showMissingTask()
            return
        }
        owner.startEditTastActivity(taskId)
    }

    fun deleteTask() {
        if (taskId.isEmpty()) {
            views.showMissingTask()
            return
        }
        tasksRepository.deleteTask(taskId)
        owner.finishAfterDelete()
    }

    fun completeTask() {
        if (taskId.isEmpty()) {
            views.showMissingTask()
            return
        }
        tasksRepository.completeTask(taskId)
        views.showTaskMarkedComplete()
    }

    fun activateTask() {
        if (taskId.isEmpty()) {
            views.showMissingTask()
            return
        }
        tasksRepository.activateTask(taskId)
        views.showTaskMarkedActive()
    }

    private fun showTask(task: Task) {
        with(views) {
            if (taskId.isEmpty()) {
                hideTitle()
                hideDescription()
            } else {
                showTitle(task.title)
                showDescription(task.description)
            }
            showCompletionStatus(task.isCompleted)
        }
    }

    override fun onClickFabEditTask() {
        editTask()
    }

    override fun onTaskCompleteChecked() {
        completeTask()
    }

    override fun onTaskActivateChecked() {
        activateTask()
    }
}