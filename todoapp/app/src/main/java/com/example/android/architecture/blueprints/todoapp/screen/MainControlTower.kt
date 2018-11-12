package com.example.android.architecture.blueprints.todoapp.screen

import com.example.android.architecture.blueprints.todoapp.screen.tasks.TasksFragment
import com.naver.android.svc.core.controltower.ControlTower

/**
 * @author bs.nam@navercorp.com
 */
class MainControlTower(screen: MainActivity, views: MainViews) : ControlTower<MainActivity, MainViews>(screen, views), MainViewsAction {

    override fun onCreated() {}

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