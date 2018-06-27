package com.example.android.architecture.blueprints.todoapp.screen.addedittask

import android.support.design.widget.Snackbar
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.util.showSnackBar
import com.naver.android.svc.core.views.UseCaseViews
import kotlinx.android.synthetic.main.activity_add_edit_task.*
import kotlinx.android.synthetic.main.activity_add_edit_task.view.*

/**
 * @author bs.nam@navercorp.com
 */
class AddEditTaskViews(screen: AddEditTaskActivity) : UseCaseViews<AddEditTaskUseCase>(screen) {

    val title by lazy { screen.add_task_title }
    val description by lazy { screen.add_task_description }

    override val layoutResId: Int
        get() = R.layout.activity_add_edit_task
    val isActive: Boolean
        get() = screen.isActive
    var isEditMode: Boolean = false

    override fun onCreated() {
        rootView.fab_edit_task_done.setOnClickListener {
            useCase.onClickFabEditTaskDone(title.text.toString(), description.text.toString())
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