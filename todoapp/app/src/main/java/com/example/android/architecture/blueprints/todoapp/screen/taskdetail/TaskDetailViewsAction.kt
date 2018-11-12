package com.example.android.architecture.blueprints.todoapp.screen.taskdetail

import com.naver.android.svc.core.views.ViewsAction

/**
 * @author bs.nam@navercorp.com
 */
interface TaskDetailViewsAction : ViewsAction {
    fun onClickFabEditTask()
    fun onTaskCompleteChecked()
    fun onTaskActivateChecked()
}