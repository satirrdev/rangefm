package com.satirr.commons.compose.extensions

import android.content.Context
import com.satirr.commons.helpers.BaseConfig

val Context.config: BaseConfig get() = BaseConfig.newInstance(applicationContext)
