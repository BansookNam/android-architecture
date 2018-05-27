/*
 * Copyright (C) 2017 The Android Open Source Project
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
package com.example.android.architecture.blueprints.todoapp.util


import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity

const val BACK_STACK_ROOT_NAME = "root_fragment"

fun AppCompatActivity.replaceRootFragmentInActivity(fragment: Fragment, @IdRes frameId: Int) {
    replaceFragmentInActivity(fragment, frameId, false)
}

fun AppCompatActivity.replaceFragmentInActivity(fragment: Fragment, @IdRes frameId: Int) {
    replaceFragmentInActivity(fragment, frameId, true)
}

fun AppCompatActivity.replaceFragmentInActivity(fragment: Fragment, @IdRes frameId: Int, addToBackStack: Boolean) {
    supportFragmentManager.transact {
        replace(frameId, fragment, fragment.javaClass.simpleName)
        if (addToBackStack) {
            addToBackStack(BACK_STACK_ROOT_NAME)
        }
    }
}

fun AppCompatActivity.clearAllFragments() {
    supportFragmentManager.popBackStack(BACK_STACK_ROOT_NAME, FragmentManager.POP_BACK_STACK_INCLUSIVE)
}

/**
 * The `fragment` is added to the container view with tag. The operation is
 * performed by the `fragmentManager`.
 */
fun AppCompatActivity.addFragmentToActivity(fragment: Fragment, @IdRes frameId: Int, tag: String) {
    supportFragmentManager.transact {
        val previousTag = supportFragmentManager.findFragmentById(frameId).tag
        add(frameId, fragment, tag)
        addToBackStack(BACK_STACK_ROOT_NAME)
    }
}

fun AppCompatActivity.setupActionBar(@IdRes toolbarId: Int, action: ActionBar.() -> Unit) {
    setSupportActionBar(findViewById(toolbarId))
    supportActionBar?.run {
        action()
    }
}

/**
 * Runs a FragmentTransaction, then calls commit().
 */
private inline fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        action()
    }.commit()
}