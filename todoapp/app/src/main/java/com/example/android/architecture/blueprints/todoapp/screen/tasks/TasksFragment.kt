package com.example.android.architecture.blueprints.todoapp.screen.tasks

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.PopupMenu
import android.view.MenuItem
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.screen.MainActivity
import com.example.android.architecture.blueprints.todoapp.screen.addedittask.AddEditTaskActivity
import com.example.android.architecture.blueprints.todoapp.taskdetail.TaskDetailActivity
import com.naver.android.svc.core.SvcBaseFragment

/**
 * @author bs.nam@navercorp.com
 */
class TasksFragment : SvcBaseFragment<TasksViews, TasksCT>() {

    private val CURRENT_FILTERING_KEY = "CURRENT_FILTERING_KEY"

    override fun createControlTower() = TasksCT(this, views)
    override fun createViews() = TasksViews(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).supportActionBar?.setTitle(R.string.list_title)
        setHasOptionsMenu(true)
        if (savedInstanceState != null) {
            ct.currentFiltering = savedInstanceState.getSerializable(CURRENT_FILTERING_KEY)
                    as TasksFilterType
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState?.apply {
            putSerializable(CURRENT_FILTERING_KEY, ct.currentFiltering)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        ct.result(requestCode, resultCode)
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
        startActivity(intent)
    }

    fun startEditTaskActivity() {
        val intent = Intent(context, AddEditTaskActivity::class.java)
        startActivityForResult(intent, AddEditTaskActivity.REQUEST_ADD_TASK)
    }

}