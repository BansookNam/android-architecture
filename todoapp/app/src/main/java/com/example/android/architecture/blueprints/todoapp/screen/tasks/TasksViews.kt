package com.example.android.architecture.blueprints.todoapp.screen.tasks

import com.example.android.architecture.blueprints.todoapp.R
import com.naver.android.svc.core.views.UseCaseViews

/**
 * @author bs.nam@navercorp.com
 */
class TasksViews(owner: TasksFragment) : UseCaseViews<TasksFragment, TasksUseCase>(owner) {

    override val layoutResId: Int
        get() = R.layout.fragment_tasks

    override fun onCreated() {
    }
}