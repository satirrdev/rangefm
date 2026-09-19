package com.satirr.range.dialogs

import com.satirr.commons.activities.BaseSimpleActivity
import com.satirr.commons.extensions.getAlertDialogBuilder
import com.satirr.commons.extensions.setupDialogStuff
import com.satirr.commons.helpers.TAB_FILES
import com.satirr.commons.helpers.TAB_RECENT_FILES
import com.satirr.commons.helpers.TAB_STORAGE_ANALYSIS
import com.satirr.commons.views.MyAppCompatCheckbox
import com.satirr.range.R
import com.satirr.range.databinding.DialogManageVisibleTabsBinding
import com.satirr.range.extensions.config
import com.satirr.range.helpers.ALL_TABS_MASK

class ManageVisibleTabsDialog(val activity: BaseSimpleActivity) {
    private val binding = DialogManageVisibleTabsBinding.inflate(activity.layoutInflater)
    private val tabs = LinkedHashMap<Int, Int>()

    init {
        tabs.apply {
            put(TAB_FILES, R.id.manage_visible_tabs_files)
            put(TAB_RECENT_FILES, R.id.manage_visible_tabs_recent_files)
            put(TAB_STORAGE_ANALYSIS, R.id.manage_visible_tabs_storage_analysis)
        }

        val showTabs = activity.config.showTabs
        for ((key, value) in tabs) {
            binding.root.findViewById<MyAppCompatCheckbox>(value).isChecked = showTabs and key != 0
        }

        activity.getAlertDialogBuilder()
            .setPositiveButton(R.string.ok) { dialog, which -> dialogConfirmed() }
            .setNegativeButton(R.string.cancel, null)
            .apply {
                activity.setupDialogStuff(binding.root, this)
            }
    }

    private fun dialogConfirmed() {
        var result = 0
        for ((key, value) in tabs) {
            if (binding.root.findViewById<MyAppCompatCheckbox>(value).isChecked) {
                result += key
            }
        }

        if (result == 0) {
            result = ALL_TABS_MASK
        }

        activity.config.showTabs = result
    }
}
