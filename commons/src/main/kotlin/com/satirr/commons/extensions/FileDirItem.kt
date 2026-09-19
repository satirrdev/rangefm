package com.satirr.commons.extensions

import android.content.Context
import com.satirr.commons.models.FileDirItem

fun FileDirItem.isRecycleBinPath(context: Context): Boolean {
    return path.startsWith(context.recycleBinPath)
}
