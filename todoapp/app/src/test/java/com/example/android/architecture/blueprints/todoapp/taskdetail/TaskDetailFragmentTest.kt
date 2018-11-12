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
import com.example.android.architecture.blueprints.todoapp.data.source.TasksRepository
import com.example.android.architecture.blueprints.todoapp.eq
import com.example.android.architecture.blueprints.todoapp.screen.taskdetail.TaskDetailActivity
import com.example.android.architecture.blueprints.todoapp.screen.taskdetail.TaskDetailControlTower
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
class TaskDetailFragmentTest {

    private val TITLE_TEST = "title"

    private val DESCRIPTION_TEST = "description"

    private val INVALID_TASK_ID = ""

    private val ACTIVE_TASK = Task(TITLE_TEST, DESCRIPTION_TEST)

    private val COMPLETED_TASK = Task(TITLE_TEST, DESCRIPTION_TEST).apply { isCompleted = true }

    @Mock
    private lateinit var views: TaskDetailViews
    @Mock
    private lateinit var screen: TaskDetailActivity
    @Mock
    private lateinit var tasksRepository: TasksRepository

    /**
     * [ArgumentCaptor] is a powerful Mockito API to capture argument values and use them to
     * perform further actions or assertions on them.
     */
    @Captor private lateinit var getTaskCallbackCaptor:
            ArgumentCaptor<TasksDataSource.GetTaskCallback>

    private lateinit var taskDetailCT: TaskDetailControlTower

    @Before fun setup() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this)

        // The presenter won't update the view unless it's active.
        `when`(screen.isActive).thenReturn(true)
        `when`(views.isInitialized).thenReturn(true)
        `when`(views.screen).thenReturn(screen)
    }

    @Test fun getActiveTaskFromRepositoryAndLoadIntoView() {
        // When tasks presenter is asked to open a task
        taskDetailCT = TaskDetailControlTower(screen, views, ACTIVE_TASK.id, tasksRepository)
                .apply {
                    onCreated()
                }
        // Then task is loaded from model, callback is captured and progress indicator is shown
        verify(tasksRepository).getTask(eq(ACTIVE_TASK.id),
                capture(getTaskCallbackCaptor))
        val inOrder = inOrder(views)
        inOrder.verify(views).setLoadingIndicator(true)

        // When task is finally loaded
        getTaskCallbackCaptor.value.onTaskLoaded(ACTIVE_TASK) // Trigger callback

        // Then progress indicator is hidden and title, description and completion status are shown
        // in UI
        inOrder.verify(views).setLoadingIndicator(false)
        verify(views).showTitle(TITLE_TEST)
        verify(views).showDescription(DESCRIPTION_TEST)
        verify(views).showCompletionStatus(false)
    }

    @Test fun getCompletedTaskFromRepositoryAndLoadIntoView() {
        taskDetailCT = TaskDetailControlTower(screen, views, COMPLETED_TASK.id, tasksRepository)
                .apply { onCreated() }

        // Then task is loaded from model, callback is captured and progress indicator is shown
        verify(tasksRepository).getTask(
                eq(COMPLETED_TASK.id), capture(getTaskCallbackCaptor))
        val inOrder = inOrder(views)
        inOrder.verify(views).setLoadingIndicator(true)

        // When task is finally loaded
        getTaskCallbackCaptor.value.onTaskLoaded(COMPLETED_TASK) // Trigger callback

        // Then progress indicator is hidden and title, description and completion status are shown
        // in UI
        inOrder.verify(views).setLoadingIndicator(false)
        verify(views).showTitle(TITLE_TEST)
        verify(views).showDescription(DESCRIPTION_TEST)
        verify(views).showCompletionStatus(true)
    }

    @Test fun getUnknownTaskFromRepositoryAndLoadIntoView() {
        // When loading of a task is requested with an invalid task ID.
        taskDetailCT = TaskDetailControlTower(screen, views, INVALID_TASK_ID, tasksRepository)
                .apply { onCreated() }
        verify(views).showMissingTask()
    }

    @Test fun deleteTask() {
        // Given an initialized TaskDetailPresenter with stubbed task
        val task = Task(TITLE_TEST, DESCRIPTION_TEST)

        // When the deletion of a task is requested
        taskDetailCT = TaskDetailControlTower(screen, views, task.id, tasksRepository)
                .apply { deleteTask() }

        // Then the repository and the view are notified
        verify(tasksRepository).deleteTask(task.id)
        verify(screen).finishAfterDelete()
    }

    @Test fun completeTask() {
        // Given an initialized presenter with an active task
        val task = Task(TITLE_TEST, DESCRIPTION_TEST)
        taskDetailCT = TaskDetailControlTower(screen, views, task.id, tasksRepository)

        taskDetailCT.apply {
            onCreated()
            completeTask()
        }

        // Then a request is sent to the task repository and the UI is updated
        verify(tasksRepository).completeTask(task.id)
        verify(views).showTaskMarkedComplete()
    }

    @Test fun activateTask() {
        // Given an initialized presenter with a completed task
        val task = Task(TITLE_TEST, DESCRIPTION_TEST).apply { isCompleted = true }
        taskDetailCT = TaskDetailControlTower(screen, views, task.id, tasksRepository)
                .apply {
                    onCreated()
                    activateTask()
                }

        val tasksRepository = taskDetailCT.tasksRepository

        // Then a request is sent to the task repository and the UI is updated
        verify(tasksRepository).activateTask(task.id)
        verify(views).showTaskMarkedActive()
    }

    @Test fun activeTaskIsShownWhenEditing() {
        // When the edit of an ACTIVE_TASK is requested
        taskDetailCT = TaskDetailControlTower(
                screen, views, ACTIVE_TASK.id, tasksRepository).apply { editTask() }

        // Then the view is notified
        verify(screen).startEditTastActivity(ACTIVE_TASK.id)
    }

    @Test fun invalidTaskIsNotShownWhenEditing() {
        // When the edit of an invalid task id is requested
        taskDetailCT = TaskDetailControlTower(
                screen, views, INVALID_TASK_ID, tasksRepository).apply { editTask() }

        // Then the edit mode is never started
        verify(screen, never()).startEditTastActivity(INVALID_TASK_ID)
        // instead, the error is shown.
        verify(views).showMissingTask()
    }
}
