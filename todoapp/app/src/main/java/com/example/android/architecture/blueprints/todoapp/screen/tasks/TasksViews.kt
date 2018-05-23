package com.example.android.architecture.blueprints.todoapp.screen.tasks

import android.support.design.widget.Snackbar
import android.view.View
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.util.showSnackBar
import com.naver.android.svc.core.views.UseCaseViews
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_tasks.*
import java.util.*

/**
 * @author bs.nam@navercorp.com
 */
class TasksViews(owner: TasksFragment) : UseCaseViews<TasksFragment, TasksUseCase>(owner) {

    override val layoutResId: Int
        get() = R.layout.fragment_tasks


    var isActive: Boolean = false
        get() = owner.isAvailable()

    val noTasksView by lazy { owner.noTasks }
    val noTaskIcon by lazy { owner.noTasksIcon }
    val noTaskMainView by lazy { owner.noTasksMain }
    val noTaskAddView by lazy { owner.noTasksAdd }
    val tasksView by lazy { owner.tasksLL }
    val filteringLabelView by lazy { owner.filteringLabel }

    /**
     * should initialize "onCreated" because useCase instance setted when "onCreated"
     * or use lazy initialize.
     */
    val listAdapter by lazy { TasksAdapter(ArrayList(0), usecase) }


    override fun onCreated() {

        val listView = owner.tasks_list.apply {
            adapter = listAdapter
        }

        // Set up progress indicator
        owner.refresh_layout.apply {
            setColorSchemeColors(
                    getColor(R.color.colorPrimary),
                    getColor(R.color.colorAccent),
                    getColor(R.color.colorPrimaryDark)
            )
            // Set the scrolling view in the custom SwipeRefreshLayout.
            scrollUpChild = listView
            setOnRefreshListener { usecase.onRefresh() }
        }

        noTaskAddView.setOnClickListener {
            usecase.onClickTaskAdd()
        }
    }

    fun setLoadingIndicator(active: Boolean) {
        if (isDestroyed()) {
            return
        }

        with(owner.refresh_layout) {
            // Make sure setRefreshing() is called after the layout is done with everything else.
            post { isRefreshing = active }
        }
    }

    fun showTasks(tasks: List<Task>) {
        listAdapter.tasks = tasks
        tasksView.visibility = View.VISIBLE
        noTasksView.visibility = View.GONE
    }

    fun showNoActiveTasks() {
        showNoTasksViews(getString(R.string.no_tasks_active), R.drawable.ic_check_circle_24dp, false)
    }

    fun showNoTasks() {
        showNoTasksViews(getString(R.string.no_tasks_all), R.drawable.ic_assignment_turned_in_24dp, false)
    }

    fun showNoCompletedTasks() {
        showNoTasksViews(getString(R.string.no_tasks_completed), R.drawable.ic_verified_user_24dp, false)
    }

    fun showSuccessfullySavedMessage() {
        showMessage(getString(R.string.successfully_saved_task_message))
    }

    private fun showNoTasksViews(mainText: String, iconRes: Int, showAddView: Boolean) {
        tasksView.visibility = View.GONE
        noTasksView.visibility = View.VISIBLE

        noTaskMainView.text = mainText
        noTaskIcon.setImageResource(iconRes)
        noTaskAddView.visibility = if (showAddView) View.VISIBLE else View.GONE
    }

    fun showActiveFilterLabel() {
        filteringLabelView.text = getString(R.string.label_active)
    }

    fun showCompletedFilterLabel() {
        filteringLabelView.text = getString(R.string.label_completed)
    }

    fun showAllFilterLabel() {
        filteringLabelView.text = getString(R.string.label_all)
    }

    fun showTaskMarkedComplete() {
        showMessage(getString(R.string.task_marked_complete))
    }

    fun showTaskMarkedActive() {
        showMessage(getString(R.string.task_marked_active))
    }

    fun showCompletedTasksCleared() {
        showMessage(getString(R.string.completed_tasks_cleared))
    }

    fun showLoadingTasksError() {
        showMessage(getString(R.string.loading_tasks_error))
    }

    private fun showMessage(message: String) {
        rootView?.showSnackBar(message, Snackbar.LENGTH_LONG)
    }


}