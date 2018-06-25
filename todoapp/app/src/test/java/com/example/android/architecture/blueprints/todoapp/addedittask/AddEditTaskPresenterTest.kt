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

package com.example.android.architecture.blueprints.todoapp.addedittask

import com.example.android.architecture.blueprints.todoapp.any
import com.example.android.architecture.blueprints.todoapp.capture
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.TasksDataSource
import com.example.android.architecture.blueprints.todoapp.eq
import com.example.android.architecture.blueprints.todoapp.screen.addedittask.AddEditTaskActivity
import com.example.android.architecture.blueprints.todoapp.screen.addedittask.AddEditTaskCT
import com.example.android.architecture.blueprints.todoapp.screen.addedittask.AddEditTaskViews
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

/**
 * Unit tests for the implementation of [AddEditTaskPresenter].
 */
class AddEditTaskPresenterTest {

    @Mock
    private lateinit var screen: AddEditTaskActivity
    @Mock
    private lateinit var tasksRepository: TasksDataSource
    @Mock
    private lateinit var addEditTaskViews: AddEditTaskViews

    /**
     * [ArgumentCaptor] is a powerful Mockito API to capture argument values and use them to
     * perform further actions or assertions on them.
     */
    @Captor private lateinit var getTaskCallbackCaptor: ArgumentCaptor<TasksDataSource.GetTaskCallback>

    private lateinit var addEditTaskCT: AddEditTaskCT

    @Before fun setupMocksAndView() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this)

        // The presenter wont't update the view unless it's active.
        `when`(addEditTaskViews.isActive).thenReturn(true)
    }

    @Test fun createPresenter_setsThePresenterToView() {
        // Get a reference to the class under test
        addEditTaskCT = AddEditTaskCT(screen, addEditTaskViews, tasksRepository)

        // Then the presenter is set to the view
        verify(addEditTaskViews).useCase = addEditTaskCT
    }

    @Test fun saveNewTaskToRepository_showsSuccessMessageUi() {
        // Get a reference to the class under test
        addEditTaskCT = AddEditTaskCT(screen, addEditTaskViews, tasksRepository)

        // When the presenter is asked to save a task
        addEditTaskCT.saveTask("New Task Title", "Some Task Description")

        // Then a task is saved in the repository and the view updated
        verify(tasksRepository).saveTask(any<Task>()) // saved to the model
        verify(screen).showTasksList() // shown in the UI
    }

    @Test fun saveTask_emptyTaskShowsErrorUi() {
        // Get a reference to the class under test
        addEditTaskCT = AddEditTaskCT(screen, addEditTaskViews, tasksRepository)

        // When the presenter is asked to save an empty task
        addEditTaskCT.saveTask("", "")

        // Then an empty not error is shown in the UI
        verify(addEditTaskViews).showEmptyTaskError()
    }

    @Test fun saveExistingTaskToRepository_showsSuccessMessageUi() {
        // Get a reference to the class under test
        addEditTaskCT = AddEditTaskCT(screen, addEditTaskViews, tasksRepository)

        // When the presenter is asked to save an existing task
        addEditTaskCT.saveTask("Existing Task Title", "Some Task Description")

        // Then a task is saved in the repository and the view updated
        verify(tasksRepository).saveTask(any<Task>()) // saved to the model
        verify(screen).showTasksList() // shown in the UI
    }

    @Test fun populateTask_callsRepoAndUpdatesView() {
        val testTask = Task("TITLE", "DESCRIPTION")
        // Get a reference to the class under test
        addEditTaskCT = AddEditTaskCT(screen, addEditTaskViews, tasksRepository).apply {
            // When the presenter is asked to populate an existing task
            taskId = testTask.id
            populateTask()
        }

        // Then the task repository is queried and the view updated
        verify(tasksRepository).getTask(eq(testTask.id), capture(getTaskCallbackCaptor))
        assertThat(addEditTaskCT.isDataMissing, `is`(true))

        // Simulate callback
        getTaskCallbackCaptor.value.onTaskLoaded(testTask)

        verify(addEditTaskViews).setTitle(testTask.title)
        verify(addEditTaskViews).setDescription(testTask.description)
        assertThat(addEditTaskCT.isDataMissing, `is`(false))
    }
}
