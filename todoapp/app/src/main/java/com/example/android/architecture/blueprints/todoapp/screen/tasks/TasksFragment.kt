package com.example.android.architecture.blueprints.todoapp.screen.tasks

import com.naver.android.svc.core.SvcBaseFragment

/**
 * @author bs.nam@navercorp.com
 */
class TasksFragment : SvcBaseFragment<TasksViews, TasksCT>() {

    override fun createControlTower() = TasksCT(this, views)
    override fun createViews() = TasksViews(this)
}