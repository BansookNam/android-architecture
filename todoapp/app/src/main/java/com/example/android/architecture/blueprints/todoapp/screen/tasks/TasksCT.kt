package com.example.android.architecture.blueprints.todoapp.screen.tasks

import com.naver.android.svc.core.SvcBaseCT

/**
 * @author bs.nam@navercorp.com
 */
class TasksCT(owner: TasksFragment, views: TasksViews) : SvcBaseCT<TasksFragment, TasksViews>(owner, views), TasksUseCase {

    override fun onCreated() {
    }
}