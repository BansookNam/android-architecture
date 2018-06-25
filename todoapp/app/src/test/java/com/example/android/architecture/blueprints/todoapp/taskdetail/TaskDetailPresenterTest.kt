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
package com.example.android.architecture.blueprints.todoapp.taskdetail

import com.example.android.architecture.blueprints.todoapp.capture
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.TasksDataSource
import com.example.android.architecture.blueprints.todoapp.eq
import com.example.android.architecture.blueprints.todoapp.screen.taskdetail.TaskDetailActivity
import com.example.android.architecture.blueprints.todoapp.screen.taskdetail.TaskDetailCT
import com.example.android.architecture.blueprints.todoapp.screen.taskdetail.TaskDetailViews
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

/**
 * Unit tests for the implementation of [TaskDetailPresenter]
 */
class TaskDetailPresenterTest {

    private val TITLE_TEST = "title"

    private val DESCRIPTION_TEST = "description"

    private val INVALID_TASK_ID = ""

    private val ACTIVE_TASK = Task(TITLE_TEST, DESCRIPTION_TEST)

    private val COMPLETED_TASK = Task(TITLE_TEST, DESCRIPTION_TEST).apply { isCompleted = true }

    @Mock
    private lateinit var taskDetailViews: TaskDetailViews
    @Mock
    private lateinit var screen: TaskDetailActivity

    /**
     * [ArgumentCaptor] is a powerful Mockito API to capture argument values and use them to
     * perform further actions or assertions on them.
     */
    @Captor private lateinit var getTaskCallbackCaptor:
            ArgumentCaptor<TasksDataSource.GetTaskCallback>

    private lateinit var taskDetailCT: TaskDetailCT

    @Before fun setup() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this)

        // The presenter won't update the view unless it's active.
        `when`(taskDetailViews.isInitialized).thenReturn(true)
    }

    @Test fun createPresenter_setsThePresenterToView() {
        // Get a reference to the class under test
        taskDetailCT = TaskDetailCT(screen, taskDetailViews, ACTIVE_TASK.id)

        // Then the presenter is set to the view
        verify(taskDetailViews).useCase = taskDetailCT
    }

    @Test fun getActiveTaskFromRepositoryAndLoadIntoView() {
        // When tasks presenter is asked to open a task
        taskDetailCT = TaskDetailCT(
                screen, taskDetailViews, ACTIVE_TASK.id).apply {
            onCreated()
        }
        val tasksRepository = taskDetailCT.tasksRepository

        // Then task is loaded from model, callback is captured and progress indicator is shown
        verify(tasksRepository).getTask(eq(ACTIVE_TASK.id),
                capture(getTaskCallbackCaptor))
        val inOrder = inOrder(taskDetailViews)
        inOrder.verify(taskDetailViews).setLoadingIndicator(true)

        // When task is finally loaded
        getTaskCallbackCaptor.value.onTaskLoaded(ACTIVE_TASK) // Trigger callback

        // Then progress indicator is hidden and title, description and completion status are shown
        // in UI
        inOrder.verify(taskDetailViews).setLoadingIndicator(false)
        verify(taskDetailViews).showTitle(TITLE_TEST)
        verify(taskDetailViews).showDescription(DESCRIPTION_TEST)
        verify(taskDetailViews).showCompletionStatus(false)
    }

    @Test fun getCompletedTaskFromRepositoryAndLoadIntoView() {
        taskDetailCT = TaskDetailCT(
                screen, taskDetailViews, COMPLETED_TASK.id).apply { onCreated() }

        val tasksRepository = taskDetailCT.tasksRepository

        // Then task is loaded from model, callback is captured and progress indicator is shown
        verify(tasksRepository).getTask(
                eq(COMPLETED_TASK.id), capture(getTaskCallbackCaptor))
        val inOrder = inOrder(taskDetailViews)
        inOrder.verify(taskDetailViews).setLoadingIndicator(true)

        // When task is finally loaded
        getTaskCallbackCaptor.value.onTaskLoaded(COMPLETED_TASK) // Trigger callback

        // Then progress indicator is hidden and title, description and completion status are shown
        // in UI
        inOrder.verify(taskDetailViews).setLoadingIndicator(false)
        verify(taskDetailViews).showTitle(TITLE_TEST)
        verify(taskDetailViews).showDescription(DESCRIPTION_TEST)
        verify(taskDetailViews).showCompletionStatus(true)
    }

    @Test fun getUnknownTaskFromRepositoryAndLoadIntoView() {
        // When loading of a task is requested with an invalid task ID.
        taskDetailCT = TaskDetailCT(
                screen, taskDetailViews, INVALID_TASK_ID).apply { onCreated() }
        verify(taskDetailViews).showMissingTask()
    }

    @Test fun deleteTask() {
        // Given an initialized TaskDetailPresenter with stubbed task
        val task = Task(TITLE_TEST, DESCRIPTION_TEST)

        // When the deletion of a task is requested
        taskDetailCT = TaskDetailCT(
                screen, taskDetailViews, task.id).apply { deleteTask() }

        val tasksRepository = taskDetailCT.tasksRepository

        // Then the repository and the view are notified
        verify(tasksRepository).deleteTask(task.id)
        verify(screen).finishAfterDelete()
    }

    @Test fun completeTask() {
        // Given an initialized presenter with an active task
        val task = Task(TITLE_TEST, DESCRIPTION_TEST)
        taskDetailCT = TaskDetailCT(
                screen, taskDetailViews, task.id).apply {
            onCreated()
            completeTask()
        }

        val tasksRepository = taskDetailCT.tasksRepository

        // Then a request is sent to the task repository and the UI is updated
        verify(tasksRepository).completeTask(task.id)
        verify(taskDetailViews).showTaskMarkedComplete()
    }

    @Test fun activateTask() {
        // Given an initialized presenter with a completed task
        val task = Task(TITLE_TEST, DESCRIPTION_TEST).apply { isCompleted = true }
        taskDetailCT = TaskDetailCT(
                screen, taskDetailViews, task.id).apply {
            onCreated()
            activateTask()
        }

        val tasksRepository = taskDetailCT.tasksRepository

        // Then a request is sent to the task repository and the UI is updated
        verify(tasksRepository).activateTask(task.id)
        verify(taskDetailViews).showTaskMarkedActive()
    }

    @Test fun activeTaskIsShownWhenEditing() {
        // When the edit of an ACTIVE_TASK is requested
        taskDetailCT = TaskDetailCT(
                screen, taskDetailViews, ACTIVE_TASK.id).apply { editTask() }

        // Then the view is notified
        verify(screen).startEditTastActivity(ACTIVE_TASK.id)
    }

    @Test fun invalidTaskIsNotShownWhenEditing() {
        // When the edit of an invalid task id is requested
        taskDetailCT = TaskDetailCT(
                screen, taskDetailViews, INVALID_TASK_ID).apply { editTask() }

        // Then the edit mode is never started
        verify(screen, never()).startEditTastActivity(INVALID_TASK_ID)
        // instead, the error is shown.
        verify(taskDetailViews).showMissingTask()
    }
}
