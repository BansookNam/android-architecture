package com.example.android.architecture.blueprints.todoapp.screen.base

import com.naver.android.svc.core.controltower.ControlTower
import com.naver.android.svc.core.screen.SvcFragment
import com.naver.android.svc.core.views.Views

abstract class BaseFragment<out V : Views, out C : ControlTower<*, *>> : SvcFragment<V, C>() {

    abstract val fragmentTitleResId: Int
}