package com.example.android.architecture.blueprints.todoapp.screen.tasks

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.PopupMenu
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.screen.addedittask.AddEditTaskActivity
import com.example.android.architecture.blueprints.todoapp.screen.base.BaseFragment
import com.example.android.architecture.blueprints.todoapp.screen.taskdetail.TaskDetailActivity

/**
 * @author bs.nam@navercorp.com
 */
class TasksFragment : BaseFragment<TasksViews, TasksCT>() {

    override val fragmentTitleResId: Int
        get() = R.string.list_title

    private val CURRENT_FILTERING_KEY = "CURRENT_FILTERING_KEY"

    override fun createControlTower() = TasksCT(this, views)
    override fun createViews() = TasksViews(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        if (savedInstanceState != null) {
            ct.currentFiltering = savedInstanceState.getSerializable(CURRENT_FILTERING_KEY)
                    as TasksFilterType
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState.apply {
            putSerializable(CURRENT_FILTERING_KEY, ct.currentFiltering)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        ct.result(requestCode, resultCode)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        inflater.inflate(R.menu.tasks_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_clear -> ct.clearCompletedTasks()
            R.id.menu_filter -> showFilteringPopUpMenu()
            R.id.menu_refresh -> ct.loadTasks(true)
        }
        return true
    }

    private fun showFilteringPopUpMenu() {
        val activity = activity ?: return
        val context = context ?: return

        PopupMenu(context, activity.findViewById(R.id.menu_filter)).apply {
            menuInflater.inflate(R.menu.filter_tasks, menu)
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.active -> ct.currentFiltering = TasksFilterType.ACTIVE_TASKS
                    R.id.completed -> ct.currentFiltering = TasksFilterType.COMPLETED_TASKS
                    else -> ct.currentFiltering = TasksFilterType.ALL_TASKS
                }
                ct.loadTasks(false)
                true
            }
            show()
        }
    }

    fun startTaskDetailActivity(taskId: String) {
        // in it's own Activity, since it makes more sense that way and it gives us the flexibility
        // to show some Intent stubbing.
        val intent = Intent(context, TaskDetailActivity::class.java).apply {
            putExtra(TaskDetailActivity.EXTRA_TASK_ID, taskId)
        }
        startActivityForResult(intent, REQUEST_TASK_DETAIL)
    }

    fun startEditTaskActivity() {
        val intent = Intent(context, AddEditTaskActivity::class.java)
        startActivityForResult(intent, REQUEST_ADD_TASK)
    }

    companion object {
        const val REQUEST_ADD_TASK = 1
        const val REQUEST_TASK_DETAIL = 2
    }
}