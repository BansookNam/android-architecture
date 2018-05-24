package com.example.android.architecture.blueprints.todoapp.screen.addedittask

import com.naver.android.svc.core.views.UseCase

interface AddEditTaskUseCase : UseCase{
    fun onClickFabEditTaskDone(title: String, description: String)

}