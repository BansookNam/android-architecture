/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.architecture.blueprints.todoapp.tasks

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleRegistry
import android.content.Context
import android.support.v4.app.FragmentActivity
import com.example.android.architecture.blueprints.todoapp.any
import com.example.android.architecture.blueprints.todoapp.argumentCaptor
import com.example.android.architecture.blueprints.todoapp.capture
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.TasksDataSource.LoadTasksCallback
import com.example.android.architecture.blueprints.todoapp.data.source.TasksRepository
import com.example.android.architecture.blueprints.todoapp.screen.tasks.*
import com.google.common.collect.Lists
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations


/**
 * Unit tests for the implementation of [TasksPresenter]
 */
class TasksFragmentTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var tasksRepository: TasksRepository
    @Mock
    private lateinit var tasksViews: TasksViews
    @Mock
    private lateinit var screen: TasksFragment

    private lateinit var lifeCycle: LifecycleRegistry
    @Mock
    private var activity: FragmentActivity? = null
    @Mock
    private lateinit var applicationContext: Context
    private val tasksViewModel: TasksViewModel = TasksViewModel()

    /**
     * [ArgumentCaptor] is a powerful Mockito API to capture argument values and use them to
     * perform further actions or assertions on them.
     */
    @Captor private lateinit var loadTasksCallbackCaptor: ArgumentCaptor<LoadTasksCallback>

    private lateinit var tasksCT: TasksControlTower

    private lateinit var tasks: MutableList<Task>

    @Before fun setupTasksPresenter() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this)

        lifeCycle = LifecycleRegistry(screen)

        `when`(tasksViews.isInitialized).thenReturn(true)
        `when`(tasksViews.isDestroyed).thenReturn(false)
        // The views won't update the view unless screen is active.
        `when`(screen.isActive).thenReturn(true)
        `when`(screen.hostActivity).thenReturn(activity)
        `when`(screen.hostActivity!!.applicationContext).thenReturn(applicationContext)
        `when`(screen.lifecycle).thenReturn(lifeCycle)

        // Get a reference to the class under test
        tasksCT = TasksControlTower(screen, tasksViews, tasksViewModel, tasksRepository)
        tasksViewModel.tasksRepository = tasksRepository
        lifeCycle.handleLifecycleEvent(Lifecycle.Event.ON_START)


        // We start the tasks to 3, with one active and two completed
        tasks = Lists.newArrayList(Task("Title1", "Description1"),
                Task("Title2", "Description2").apply { isCompleted = true },
                Task("Title3", "Description3").apply { isCompleted = true })
    }

    @Test fun loadAllTasksFromRepositoryAndLoadIntoView() {
        with(tasksCT) {
            // Given an initialized TasksPresenter with initialized tasks
            // When loading of Tasks is requested
            //`when`(lifeCycle.currentState).thenReturn(Lifecycle.State.STARTED)
            currentFiltering = TasksFilterType.ALL_TASKS
            onCreated() // regist and loadTasks observer on ViewModel
        }

        val inOrder = inOrder(tasksViews)

        // Callback is captured and invoked with stubbed tasks
        verify(tasksRepository).getTasks(capture(loadTasksCallbackCaptor))

        // Then progress indicator is shown
        inOrder.verify(tasksViews).setLoadingIndicator(true)

        loadTasksCallbackCaptor.value.onTasksLoaded(tasks)

        // Then progress indicator is hidden and all tasks are shown in UI
        inOrder.verify(tasksViews).setLoadingIndicator(false)

        val showTasksArgumentCaptor = argumentCaptor<List<Task>>()
        verify(tasksViews).showTasks(capture(showTasksArgumentCaptor))
        assertTrue(showTasksArgumentCaptor.value.size == 3)
    }

    @Test fun loadActiveTasksFromRepositoryAndLoadIntoView() {
        with(tasksCT) {
            // Given an initialized TasksPresenter with initialized tasks
            // When loading of Tasks is requested
            currentFiltering = TasksFilterType.ACTIVE_TASKS
            onCreated()
        }

        // Callback is captured and invoked with stubbed tasks
        verify(tasksRepository).getTasks(capture(loadTasksCallbackCaptor))
        loadTasksCallbackCaptor.value.onTasksLoaded(tasks)

        // Then progress indicator is hidden and active tasks are shown in UI
        verify(tasksViews).setLoadingIndicator(false)
        val showTasksArgumentCaptor = argumentCaptor<List<Task>>()
        verify(tasksViews).showTasks(capture(showTasksArgumentCaptor))
        assertTrue(showTasksArgumentCaptor.value.size == 1)
    }

    @Test fun loadCompletedTasksFromRepositoryAndLoadIntoView() {
        with(tasksCT) {
            // Given an initialized TasksPresenter with initialized tasks
            // When loading of Tasks is requested
            currentFiltering = TasksFilterType.COMPLETED_TASKS
            onCreated()
        }

        // Callback is captured and invoked with stubbed tasks
        verify(tasksRepository).getTasks(capture(loadTasksCallbackCaptor))
        loadTasksCallbackCaptor.value.onTasksLoaded(tasks)

        // Then progress indicator is hidden and completed tasks are shown in UI
        verify(tasksViews).setLoadingIndicator(false)
        val showTasksArgumentCaptor = argumentCaptor<List<Task>>()
        verify(tasksViews).showTasks(capture(showTasksArgumentCaptor))
        assertTrue(showTasksArgumentCaptor.value.size == 2)
    }

    @Test fun clickOnFab_ShowsAddTaskUi() {
        // When adding a new task
        tasksCT.onClickTaskAdd()

        // Then add task UI is shown
        verify(screen).startEditTaskActivity()
    }

    @Test fun clickOnTask_ShowsDetailUi() {
        // Given a stubbed active task
        val requestedTask = Task("Details Requested", "For this task")

        // When open task details is requested
        tasksCT.openTaskDetails(requestedTask)

        // Then task detail UI is shown
        verify(screen).startTaskDetailActivity(any<String>())
    }

    @Test fun completeTask_ShowsTaskMarkedComplete() {
        // Given a stubbed task
        val task = Task("Details Requested", "For this task")

        // When task is marked as complete
        tasksCT.completeTask(task)

        // Then repository is called and task marked complete UI is shown
        verify(tasksRepository).completeTask(task)
        verify(tasksViews).showTaskMarkedComplete()
    }

    @Test fun activateTask_ShowsTaskMarkedActive() {
        // Given a stubbed completed task
        val task = Task("Details Requested", "For this task").apply { isCompleted = true }
        with(tasksCT) {
            loadTasks(true)

            // When task is marked as activated
            activateTask(task)
        }

        // Then repository is called and task marked active UI is shown
        verify(tasksRepository).activateTask(task)
        verify(tasksViews).showTaskMarkedActive()
    }

    @Test fun unavailableTasks_ShowsError() {
        with(tasksCT) {
            // When tasks are loaded
            currentFiltering = TasksFilterType.ALL_TASKS
            loadTasks(true)
        }
        // And the tasks aren't available in the repository
        verify(tasksRepository).getTasks(capture(loadTasksCallbackCaptor))
        loadTasksCallbackCaptor.value.onDataNotAvailable()

        // Then an error message is shown
        verify(tasksViews).showLoadingTasksError()
    }
}
