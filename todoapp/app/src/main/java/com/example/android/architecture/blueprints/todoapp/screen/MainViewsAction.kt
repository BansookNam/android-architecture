package com.example.android.architecture.blueprints.todoapp.screen

import com.naver.android.svc.core.views.ViewsAction

/**
 * @author bs.nam@navercorp.com
 */
interface MainViewsAction : ViewsAction {
    fun onClickStatisticMenu()
    fun onClickFabTaskAdd()
    fun onClickTaskListMenu()

}