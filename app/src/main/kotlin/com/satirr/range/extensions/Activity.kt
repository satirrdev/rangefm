package com.satirr.range.extensions

import android.app.Activity
import android.content.Intent
import androidx.core.content.FileProvider
import com.satirr.commons.activities.BaseSimpleActivity
import com.satirr.commons.extensions.getFilenameFromPath
import com.satirr.commons.extensions.getMimeTypeFromUri
import com.satirr.commons.extensions.getParentPath
import com.satirr.commons.extensions.launchActivityIntent
import com.satirr.commons.extensions.openPathIntent
import com.satirr.commons.extensions.renameFile
import com.satirr.commons.extensions.setAsIntent
import com.satirr.commons.extensions.sharePathsIntent
import com.satirr.range.BuildConfig
import com.satirr.range.helpers.OPEN_AS_AUDIO
import com.satirr.range.helpers.OPEN_AS_DEFAULT
import com.satirr.range.helpers.OPEN_AS_IMAGE
import com.satirr.range.helpers.OPEN_AS_TEXT
import com.satirr.range.helpers.OPEN_AS_VIDEO
import java.io.File

fun Activity.sharePaths(paths: ArrayList<String>) {
    sharePathsIntent(paths, BuildConfig.APPLICATION_ID)
}

fun Activity.tryOpenPathIntent(path: String, forceChooser: Boolean, openAsType: Int = OPEN_AS_DEFAULT, finishActivity: Boolean = false) {
    if (!forceChooser && path.endsWith(".apk", true)) {
        val uri = FileProvider.getUriForFile(
            this, "${BuildConfig.APPLICATION_ID}.provider", File(path)
        )

        Intent().apply {
            action = Intent.ACTION_VIEW
            setDataAndType(uri, getMimeTypeFromUri(uri))
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            launchActivityIntent(this)
        }
    } else {
        openPath(path, forceChooser, openAsType)

        if (finishActivity) {
            finish()
        }
    }
}

fun Activity.openPath(path: String, forceChooser: Boolean, openAsType: Int = OPEN_AS_DEFAULT) {
    openPathIntent(path, forceChooser, BuildConfig.APPLICATION_ID, getMimeType(openAsType))
}

private fun getMimeType(type: Int) = when (type) {
    OPEN_AS_DEFAULT -> ""
    OPEN_AS_TEXT -> "text/*"
    OPEN_AS_IMAGE -> "image/*"
    OPEN_AS_AUDIO -> "audio/*"
    OPEN_AS_VIDEO -> "video/*"
    else -> "*/*"
}

fun Activity.setAs(path: String) {
    setAsIntent(path, BuildConfig.APPLICATION_ID)
}

fun BaseSimpleActivity.toggleItemVisibility(oldPath: String, hide: Boolean, callback: ((newPath: String) -> Unit)? = null) {
    val path = oldPath.getParentPath()
    var filename = oldPath.getFilenameFromPath()
    if ((hide && filename.startsWith('.')) || (!hide && !filename.startsWith('.'))) {
        callback?.invoke(oldPath)
        return
    }

    filename = if (hide) {
        ".${filename.trimStart('.')}"
    } else {
        filename.substring(1, filename.length)
    }

    val newPath = "$path/$filename"
    if (oldPath != newPath) {
        renameFile(oldPath, newPath, false) { success, useAndroid30Way ->
            callback?.invoke(newPath)
        }
    }
}
