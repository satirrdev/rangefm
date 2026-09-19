package com.satirr.commons

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import com.satirr.commons.extensions.appLockManager
import com.satirr.commons.extensions.checkUseEnglish

open class CommonsApp : Application() {

    open val isAppLockFeatureAvailable = false

    override fun onCreate() {
        super.onCreate()
        checkUseEnglish()
        setupAppLockManager()
    }

    private fun setupAppLockManager() {
        if (isAppLockFeatureAvailable) {
            ProcessLifecycleOwner.get().lifecycle.addObserver(appLockManager)
        }
    }
}
