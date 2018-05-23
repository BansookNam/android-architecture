package com.example.android.architecture.blueprints.todoapp.screen.addedittask

import com.naver.android.svc.core.SvcBaseActivity

/**
 * @author bs.nam@navercorp.com
 */
class AddEditTaskActivity : SvcBaseActivity<AddEditTaskViews, AddEditTaskCT>() {

    override fun createControlTower() = AddEditTaskCT(this, views)
    override fun createViews() = AddEditTaskViews(this)
}