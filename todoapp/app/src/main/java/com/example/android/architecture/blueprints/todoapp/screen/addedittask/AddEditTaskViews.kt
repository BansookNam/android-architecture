package com.example.android.architecture.blueprints.todoapp.screen.addedittask

import android.support.design.widget.Snackbar
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.util.setupActionBar
import com.example.android.architecture.blueprints.todoapp.util.showSnackBar
import com.naver.android.svc.core.views.UseCaseViews
import kotlinx.android.synthetic.main.activity_add_edit_task.*

/**
 * @author bs.nam@navercorp.com
 */
class AddEditTaskViews(owner: AddEditTaskActivity) : UseCaseViews<AddEditTaskActivity, AddEditTaskUseCase>(owner) {

    val title by lazy { owner.add_task_title }
    val description by lazy { owner.add_task_description }

    var taskId: String? = null

    override val layoutResId: Int
        get() = R.layout.activity_add_edit_task
    val isActive: Boolean
        get() = owner.isActive

    override fun onCreated() {

        // Set up the toolbar.
        owner.setupActionBar(R.id.toolbar) {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setTitle(if (taskId == null) R.string.add_task else R.string.edit_task)
        }

        owner.fab_edit_task_done.setOnClickListener {
            usecase.onClickFabEditTaskDone(title.text.toString(), description.text.toString())
        }
    }

    fun showEmptyTaskError() {
        title.showSnackBar(getString(R.string.empty_task_message), Snackbar.LENGTH_LONG)
    }

    fun setTitle(title: String) {
        this.title.setText(title)
    }

    fun setDescription(description: String) {
        this.description.setText(description)
    }
}