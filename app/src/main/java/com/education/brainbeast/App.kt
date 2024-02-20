package com.education.brainbeast

import android.app.Application
import com.education.brainbeast.ui.education.ui.utils.AppLogger

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        AppLogger.init()
    }

    companion object {
        var instance: App? = null
            private set
    }
}
