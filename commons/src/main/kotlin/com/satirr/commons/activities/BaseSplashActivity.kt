package com.satirr.commons.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.satirr.commons.R
import com.satirr.commons.extensions.*

abstract class BaseSplashActivity : AppCompatActivity() {
    abstract fun initActivity()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        syncGlobalConfig {
            baseConfig.apply {
                if (isAutoTheme()) {
                    val isUsingSystemDarkTheme = isSystemInDarkMode()
                    textColor = resources.getColor(if (isUsingSystemDarkTheme) R.color.theme_dark_text_color else R.color.theme_light_text_color)
                    backgroundColor =
                        resources.getColor(if (isUsingSystemDarkTheme) R.color.theme_dark_background_color else R.color.theme_light_background_color)
                }
            }

            initActivity()
        }
    }
}