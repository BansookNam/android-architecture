package com.example.android.architecture.blueprints.todoapp.screen.addedittask

import android.app.Activity
import android.os.Bundle
import com.naver.android.svc.core.SvcBaseActivity

/**
 * @author bs.nam@navercorp.com
 */
class AddEditTaskActivity : SvcBaseActivity<AddEditTaskViews, AddEditTaskCT>() {

    override fun createControlTower() = AddEditTaskCT(this, views)
    override fun createViews() = AddEditTaskViews(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        val taskId = intent.getStringExtra(ARGUMENT_EDIT_TASK_ID)
        views.isEditMode = taskId != null
        ct.taskId = taskId

        val shouldLoadDataFromRepo =
        // Prevent the presenter from loading data from the repository if this is a config change.
        // Data might not have loaded when the config change happen, so we saved the state.
                savedInstanceState?.getBoolean(AddEditTaskActivity.SHOULD_LOAD_DATA_FROM_REPO_KEY)
                        ?: true
        ct.isDataMissing = shouldLoadDataFromRepo

        super.onCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        // Save the state so that next time we know if we need to refresh data.
        super.onSaveInstanceState(outState.apply {
            putBoolean(AddEditTaskActivity.SHOULD_LOAD_DATA_FROM_REPO_KEY, ct.isDataMissing)
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun showTasksList() {
        setResult(Activity.RESULT_OK)
        finish()
    }


    companion object {
        const val SHOULD_LOAD_DATA_FROM_REPO_KEY = "SHOULD_LOAD_DATA_FROM_REPO_KEY"
        const val ARGUMENT_EDIT_TASK_ID = "EDIT_TASK_ID"
    }


}