package com.example.android.architecture.blueprints.todoapp.screen.addedittask

import android.support.design.widget.Snackbar
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.util.showSnackBar
import com.naver.android.svc.core.views.ActionViews
import kotlinx.android.synthetic.main.activity_add_edit_task.view.*

/**
 * @author bs.nam@navercorp.com
 */
class AddEditTaskViews : ActionViews<AddEditTaskViewsAction>() {

    private val title by lazy { rootView.add_task_title }
    private val description by lazy { rootView.add_task_description }

    override val layoutResId: Int
        get() = R.layout.activity_add_edit_task
    val isActive: Boolean
        get() = screen.isActive
    var isEditMode: Boolean = false

    override fun onCreated() {
        rootView.fab_edit_task_done.setOnClickListener {
            viewsAction.onClickFabEditTaskDone(title.text.toString(), description.text.toString())
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