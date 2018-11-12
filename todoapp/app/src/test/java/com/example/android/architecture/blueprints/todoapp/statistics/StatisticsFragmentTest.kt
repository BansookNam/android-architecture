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
package com.example.android.architecture.blueprints.todoapp.statistics

import com.example.android.architecture.blueprints.todoapp.capture
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.TasksDataSource
import com.example.android.architecture.blueprints.todoapp.screen.statistics.StatisticsControlTower
import com.example.android.architecture.blueprints.todoapp.screen.statistics.StatisticsFragment
import com.example.android.architecture.blueprints.todoapp.screen.statistics.StatisticsViews
import com.google.common.collect.Lists
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

/**
 * Unit tests for the implementation of [StatisticsFragmentTest]
 */
class StatisticsFragmentTest {

    @Mock
    private lateinit var statisticsView: StatisticsViews
    @Mock
    private lateinit var screen: StatisticsFragment
    @Mock
    private lateinit var tasksRepository: TasksDataSource
    /**
     * [ArgumentCaptor] is a powerful Mockito API to capture argument values and use them to
     * perform further actions or assertions on them.
     */
    @Captor private lateinit var loadTasksCallbackCaptor:
            ArgumentCaptor<TasksDataSource.LoadTasksCallback>
    private lateinit var statisticsCT: StatisticsControlTower
    private lateinit var tasks: MutableList<Task>

    @Before fun setupStatisticsPresenter() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this)

        // Get a reference to the class under test
        statisticsCT = StatisticsControlTower(screen, statisticsView, tasksRepository)
        //`when`(statisticsCT.tasksRepository).thenReturn(tasksRepository)

        // The presenter won't update the view unless it's active.
        `when`(screen.isActive).thenReturn(true)

        // We start the tasks to 3, with one active and two completed
        tasks = Lists.newArrayList(Task("Title1", "Description1"),
                Task("Title2", "Description2").apply { isCompleted = true },
                Task("Title3", "Description3").apply { isCompleted = true })
    }

    @Test fun loadEmptyTasksFromRepository_CallViewToDisplay() {
        // Given an initialized StatisticsPresenter with no tasks
        tasks.clear()

        // When loading of Tasks is requested
        statisticsCT.onCreated()

        //Then progress indicator is shown
        verify(statisticsView).setProgressIndicator(true)

        // Callback is captured and invoked with stubbed tasks
        verify(tasksRepository).getTasks(capture(loadTasksCallbackCaptor))
        loadTasksCallbackCaptor.value.onTasksLoaded(tasks)

        // Then progress indicator is hidden and correct data is passed on to the view
        verify(statisticsView).setProgressIndicator(false)
        verify(statisticsView).showStatistics(0, 0)
    }

    @Test fun loadNonEmptyTasksFromRepository_CallViewToDisplay() {
        // Given an initialized StatisticsPresenter with 1 active and 2 completed tasks

        // When loading of Tasks is requested
        statisticsCT.onCreated()

        //Then progress indicator is shown
        verify(statisticsView).setProgressIndicator(true)

        // Callback is captured and invoked with stubbed tasks
        verify(tasksRepository).getTasks(capture(loadTasksCallbackCaptor))
        loadTasksCallbackCaptor.value.onTasksLoaded(tasks)

        // Then progress indicator is hidden and correct data is passed on to the view
        verify(statisticsView).setProgressIndicator(false)
        verify(statisticsView).showStatistics(1, 2)
    }

    @Test fun loadStatisticsWhenTasksAreUnavailable_CallErrorToDisplay() {
        // When statistics are loaded
        statisticsCT.onCreated()

        // And tasks data isn't available
        verify(tasksRepository).getTasks(capture(loadTasksCallbackCaptor))
        loadTasksCallbackCaptor.value.onDataNotAvailable()

        // Then an error message is shown
        verify(statisticsView).showLoadingStatisticsError()
    }
}
