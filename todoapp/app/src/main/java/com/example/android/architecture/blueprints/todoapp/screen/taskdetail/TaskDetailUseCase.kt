package com.example.android.architecture.blueprints.todoapp.screen.taskdetail

import com.naver.android.svc.core.views.UseCase

/**
 * @author bs.nam@navercorp.com
 */
interface TaskDetailUseCase : UseCase {
    fun onClickFabEditTask()
    fun onTaskCompleteChecked()
    fun onTaskActivateChecked()
}