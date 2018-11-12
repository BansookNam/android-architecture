package com.example.android.architecture.blueprints.todoapp.screen.addedittask

import com.naver.android.svc.core.views.ViewsAction

interface AddEditTaskViewsAction : ViewsAction{
    fun onClickFabEditTaskDone(title: String, description: String)

}