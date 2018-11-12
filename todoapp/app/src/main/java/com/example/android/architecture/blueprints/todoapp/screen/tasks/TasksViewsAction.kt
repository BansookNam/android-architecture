package com.example.android.architecture.blueprints.todoapp.screen.tasks

import com.naver.android.svc.core.views.ViewsAction

/**
 * @author bs.nam@navercorp.com
 */
interface TasksViewsAction : ViewsAction, TasksAdapter.TaskItemListener {
    fun onRefresh()
    fun onClickTaskAdd()


}