package com.moneam.weatherphoto.common.navigation

import android.app.Activity
import android.content.Intent
import com.ahmoneam.basecleanarchitecture.base.platform.BaseNavigationDestination
import com.moneam.weatherphoto.MainActivity


object NavigationDestination {
    object Main : BaseNavigationDestination.Activities<Unit>(true, Unit) {
        override fun start(component: Activity) {
            val intent = Intent(component, MainActivity::class.java)
            component.startActivity(intent)
            if (finish) component.finish()
        }
    }
}


