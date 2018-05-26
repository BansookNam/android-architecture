package com.example.android.architecture.blueprints.todoapp.screen.tasks

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.android.architecture.blueprints.todoapp.data.Task

class TasksViewModel : ViewModel() {
    val tasks = MutableLiveData<MutableList<Task>>()
}