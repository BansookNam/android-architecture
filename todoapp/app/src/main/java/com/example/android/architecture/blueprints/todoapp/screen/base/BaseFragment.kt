package com.example.android.architecture.blueprints.todoapp.screen.base

import com.naver.android.svc.core.SvcBaseCT
import com.naver.android.svc.core.SvcBaseFragment
import com.naver.android.svc.core.views.SvcBaseViews

abstract class BaseFragment<V : SvcBaseViews<*>, C : SvcBaseCT<*, *>> : SvcBaseFragment<V, C>() {


    abstract val fragmentTitleResId: Int
}