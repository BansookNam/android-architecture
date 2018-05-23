package com.example.android.architecture.blueprints.todoapp.screen.addedittask

import com.example.android.architecture.blueprints.todoapp.R
import com.naver.android.svc.core.views.SvcBaseViews

/**
 * @author bs.nam@navercorp.com
 */
class AddEditTaskViews(owner: AddEditTaskActivity) : SvcBaseViews<AddEditTaskActivity>(owner) {

    override val layoutResId: Int
        get() = R.layout.activity_add_edit_task

    override fun onCreated() {
    }
}