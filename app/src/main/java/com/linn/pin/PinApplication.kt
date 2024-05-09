package com.linn.pin

import android.app.Application
import com.linn.pin.data.AppContainer
import com.linn.pin.data.AppDataContainer

class PinApplication: Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}