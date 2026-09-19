package com.satirr.range

import com.github.ajalt.reprint.core.Reprint
import com.satirr.commons.CommonsApp

class App : CommonsApp() {
    override val isAppLockFeatureAvailable = true

    override fun onCreate() {
        super.onCreate()
        Reprint.initialize(this)
    }
}
