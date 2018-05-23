package com.example.android.architecture.blueprints.todoapp.screen

import com.example.android.architecture.blueprints.todoapp.tasks.TasksFilterType
import com.naver.android.svc.core.SvcBaseCT

/**
 * @author bs.nam@navercorp.com
 */
class TasksCT(owner: TasksActivity, views: TasksViews) : SvcBaseCT<TasksActivity, TasksViews>(owner, views), TasksUseCase {

    var currentFiltering = TasksFilterType.ALL_TASKS

    override fun onCreated() {
    }

    override fun onClickStatisticMenu() {
    }
}