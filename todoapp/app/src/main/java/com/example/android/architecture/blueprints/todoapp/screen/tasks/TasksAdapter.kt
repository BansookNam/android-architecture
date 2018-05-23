package com.example.android.architecture.blueprints.todoapp.screen.tasks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.data.Task
import kotlinx.android.synthetic.main.task_item.view.*

/**
 * @author bs.nam@navercorp.com
 */
class TasksAdapter(tasks: List<Task>, private val itemListener: TaskItemListener)
    : BaseAdapter() {

    var tasks: List<Task> = tasks
        set(tasks) {
            field = tasks
            notifyDataSetChanged()
        }

    override fun getCount() = tasks.size

    override fun getItem(i: Int) = tasks[i]

    override fun getItemId(i: Int) = i.toLong()

    override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View {
        val task = getItem(i)
        val rowView = view ?: LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.task_item, viewGroup, false)

        with(rowView.title) {
            text = task.titleForList
        }

        with(rowView.complete) {
            // Active/completed task UI
            isChecked = task.isCompleted
            val rowViewBackground =
                    if (task.isCompleted) R.drawable.list_completed_touch_feedback
                    else R.drawable.touch_feedback
            rowView.setBackgroundResource(rowViewBackground)
            setOnClickListener {
                if (!task.isCompleted) {
                    itemListener.onCompleteTaskClick(task)
                } else {
                    itemListener.onActivateTaskClick(task)
                }
            }
        }
        rowView.setOnClickListener { itemListener.onTaskClick(task) }
        return rowView
    }

    interface TaskItemListener {

        fun onTaskClick(clickedTask: Task)

        fun onCompleteTaskClick(completedTask: Task)

        fun onActivateTaskClick(activatedTask: Task)
    }
}
