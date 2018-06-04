package com.example.android.architecture.blueprints.todoapp.screen

import com.example.android.architecture.blueprints.todoapp.screen.tasks.TasksFragment
import com.naver.android.svc.core.SvcCT

/**
 * @author bs.nam@navercorp.com
 */
class MainCT(screen: MainActivity, views: MainViews) : SvcCT<MainActivity, MainViews>(screen, views), MainUseCase {

    override fun onCreated() {
    }

    override fun onClickStatisticMenu() {
        screen.showStatisticFragment()
    }
    override fun onClickTaskListMenu() {
        screen.showTaskList()
    }

    override fun onClickFabTaskAdd() {
        val fragment = screen.contentFragment
        if (fragment is TasksFragment) {
            fragment.startEditTaskActivity()
        }
    }
}