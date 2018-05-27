package com.example.android.architecture.blueprints.todoapp.screen.taskdetail

import android.support.design.widget.Snackbar
import android.view.View
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.util.setupActionBar
import com.example.android.architecture.blueprints.todoapp.util.showSnackBar
import com.naver.android.svc.core.views.UseCaseViews
import kotlinx.android.synthetic.main.activity_task_detail.*

/**
 * @author bs.nam@navercorp.com
 */
class TaskDetailViews(owner: TaskDetailActivity) : UseCaseViews<TaskDetailActivity, TaskDetailUseCase>(owner) {

    private val detailTitle by lazy { owner.task_detail_title }
    private val detailDescription by lazy { owner.task_detail_description }
    private val detailCompleteStatus by lazy { owner.task_detail_complete }


    override val layoutResId: Int
        get() = R.layout.activity_task_detail

    override fun onCreated() {

        // Set up the toolbar.
        owner.setupActionBar(R.id.toolbar) {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        // Set up floating action button
        owner.fab_edit_task.setOnClickListener { usecase.onClickFabEditTask() }

    }


    fun setLoadingIndicator(active: Boolean) {
        if (active) {
            detailTitle.text = ""
            detailDescription.text = getString(R.string.loading)
        }
    }

    fun hideDescription() {
        detailDescription.visibility = View.GONE
    }

    fun hideTitle() {
        detailTitle.visibility = View.GONE
    }

    fun showDescription(description: String) {
        with(detailDescription) {
            visibility = View.VISIBLE
            text = description
        }
    }

    fun showCompletionStatus(complete: Boolean) {
        with(detailCompleteStatus) {
            isChecked = complete
            setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    usecase.onTaskCompleteChecked()
                } else {
                    usecase.onTaskActivateChecked()
                }
            }
        }
    }

    fun showTaskMarkedComplete() {
        detailTitle.showSnackBar(getString(R.string.task_marked_complete), Snackbar.LENGTH_LONG)
    }

    fun showTaskMarkedActive() {
        detailTitle.showSnackBar(getString(R.string.task_marked_active), Snackbar.LENGTH_LONG)
    }


    fun showTitle(title: String) {
        with(detailTitle) {
            visibility = View.VISIBLE
            text = title
        }
    }

    fun showMissingTask() {
        detailTitle.text = ""
        detailDescription.text = getString(R.string.no_data)
    }

}