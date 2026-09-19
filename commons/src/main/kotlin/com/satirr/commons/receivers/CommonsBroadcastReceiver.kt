package com.satirr.commons.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.satirr.commons.extensions.syncGlobalConfig
import com.satirr.commons.helpers.MyContentProvider

class CommonsBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == MyContentProvider.ACTION_GLOBAL_CONFIG_UPDATED) {
            context?.syncGlobalConfig()
        }
    }
}
