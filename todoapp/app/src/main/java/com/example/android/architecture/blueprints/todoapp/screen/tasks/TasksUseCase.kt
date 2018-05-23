package com.example.android.architecture.blueprints.todoapp.screen.tasks

import com.naver.android.svc.core.views.UseCase

/**
 * @author bs.nam@navercorp.com
 */
interface TasksUseCase : UseCase, TasksAdapter.TaskItemListener {
    fun onRefresh()
    fun onClickTaskAdd()

}