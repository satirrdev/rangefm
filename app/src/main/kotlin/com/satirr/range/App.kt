package com.satirr.range

import com.github.ajalt.reprint.core.Reprint
import com.google.android.material.color.DynamicColors
import com.google.android.material.color.DynamicColorsOptions
import com.satirr.commons.CommonsApp
import com.satirr.commons.extensions.baseConfig

class App : CommonsApp() {
    override val isAppLockFeatureAvailable = true

    override fun onCreate() {
        super.onCreate()
        val dynamicColorsOptions = DynamicColorsOptions.Builder()
            .setPrecondition { _, _ -> baseConfig.isSystemThemeEnabled }
            .build()
        DynamicColors.applyToActivitiesIfAvailable(this, dynamicColorsOptions)
        Reprint.initialize(this)
    }
}
