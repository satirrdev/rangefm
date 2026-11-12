package com.satirr.range

import com.github.ajalt.reprint.core.Reprint
import com.google.android.material.color.DynamicColors
import com.satirr.commons.CommonsApp

class App : CommonsApp() {
    override val isAppLockFeatureAvailable = true

    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
        Reprint.initialize(this)
    }
}
