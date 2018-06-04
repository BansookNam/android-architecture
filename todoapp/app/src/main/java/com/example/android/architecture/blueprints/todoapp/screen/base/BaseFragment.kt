package com.example.android.architecture.blueprints.todoapp.screen.base

import com.naver.android.svc.core.SvcCT
import com.naver.android.svc.core.screen.SvcFragment
import com.naver.android.svc.core.views.SvcViews

abstract class BaseFragment<out V : SvcViews<*>, out C : SvcCT<*, *>> : SvcFragment<V, C>() {

    abstract val fragmentTitleResId: Int
}