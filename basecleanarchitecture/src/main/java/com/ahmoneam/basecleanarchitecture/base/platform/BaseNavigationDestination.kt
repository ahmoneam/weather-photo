package com.ahmoneam.basecleanarchitecture.base.platform

import android.app.Activity
import androidx.fragment.app.Fragment

sealed class BaseNavigationDestination<T, Args>(open val finish: Boolean, open val args: Args) {
    abstract fun start(component: T)

    abstract class Activities<Args>(finish: Boolean, args: Args) :
        BaseNavigationDestination<Activity, Args>(finish, args)

    abstract class Fragments<Args>(finish: Boolean, args: Args) :
        BaseNavigationDestination<Fragment, Args>(finish, args)
}