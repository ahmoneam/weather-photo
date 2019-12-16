package com.moneam.weatherphoto.common.data.remote

import com.moneam.weatherphoto.common.data.remote.APIS.CONSTANTS.VERSION


object APIS {
    object CONSTANTS {
        const val VERSION = "2.5"
    }

    object URL {
        const val WEATHER = "$VERSION/weather"
    }
}
