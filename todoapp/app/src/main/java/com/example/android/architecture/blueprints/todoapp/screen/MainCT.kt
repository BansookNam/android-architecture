package com.example.android.architecture.blueprints.todoapp.screen

import com.example.android.architecture.blueprints.todoapp.screen.tasks.TasksFragment
import com.naver.android.svc.core.SvcBaseCT

/**
 * @author bs.nam@navercorp.com
 */
class MainCT(owner: MainActivity, views: MainViews) : SvcBaseCT<MainActivity, MainViews>(owner, views), MainUseCase {

    override fun onCreated() {
    }

    override fun onClickStatisticMenu() {
        owner.showStatisticFragment()
    }
    override fun onClickTaskListMenu() {
        owner.showTaskList()
    }

    override fun onClickFabTaskAdd() {
        val fragment = owner.contentFragment
        if (fragment is TasksFragment) {
            fragment.startEditTaskActivity()
        }
    }
}