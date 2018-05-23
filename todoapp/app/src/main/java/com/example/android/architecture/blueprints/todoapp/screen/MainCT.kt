package com.example.android.architecture.blueprints.todoapp.screen

import com.example.android.architecture.blueprints.todoapp.tasks.TasksFilterType
import com.naver.android.svc.core.SvcBaseCT

/**
 * @author bs.nam@navercorp.com
 */
class MainCT(owner: MainActivity, views: MainViews) : SvcBaseCT<MainActivity, MainViews>(owner, views), MainUseCase {

    var currentFiltering = TasksFilterType.ALL_TASKS

    override fun onCreated() {
    }

    override fun onClickStatisticMenu() {
    }
}