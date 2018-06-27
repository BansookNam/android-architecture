package com.example.android.architecture.blueprints.todoapp.screen.taskdetail

import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.TasksDataSource
import com.example.android.architecture.blueprints.todoapp.data.source.TasksRepository
import com.naver.android.svc.core.controltower.SvcCT

/**
 * @author bs.nam@navercorp.com
 */
class TaskDetailCT(screen: TaskDetailActivity, views: TaskDetailViews, val taskId: String, val tasksRepository: TasksRepository) : SvcCT<TaskDetailActivity, TaskDetailViews>(screen, views), TaskDetailUseCase {

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
                    if (!screen.isActive) {
                        return@onTaskLoaded
                    }
                    setLoadingIndicator(false)
                }
                showTask(task)
            }

            override fun onDataNotAvailable() {
                with(views) {
                    // The view may not be able to handle UI updates anymore
                    if (!screen.isActive) {
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
        screen.startEditTastActivity(taskId)
    }

    fun deleteTask() {
        if (taskId.isEmpty()) {
            views.showMissingTask()
            return
        }
        tasksRepository.deleteTask(taskId)
        screen.finishAfterDelete()
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