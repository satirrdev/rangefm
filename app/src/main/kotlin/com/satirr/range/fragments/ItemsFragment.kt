package com.satirr.range.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager
import com.satirr.commons.activities.BaseSimpleActivity
import com.satirr.commons.dialogs.StoragePickerDialog
import com.satirr.commons.extensions.*
import com.satirr.commons.helpers.*
import com.satirr.commons.models.FileDirItem
import com.satirr.commons.views.Breadcrumbs
import com.satirr.commons.views.MyGridLayoutManager
import com.satirr.commons.views.MyRecyclerView
import com.satirr.range.R
import com.satirr.range.activities.MainActivity
import com.satirr.range.activities.SimpleActivity
import com.satirr.range.adapters.ItemsAdapter
import com.satirr.range.databinding.ItemsFragmentBinding
import com.satirr.range.dialogs.CreateNewItemDialog
import com.satirr.range.extensions.config
import com.satirr.range.extensions.isPathOnRoot
import com.satirr.range.helpers.MAX_COLUMN_COUNT
import com.satirr.range.helpers.RootHelpers
import com.satirr.range.interfaces.ItemOperationsListener
import com.satirr.range.models.ListItem
import java.io.File
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.DriveFileMove
import androidx.compose.material.icons.rounded.SelectAll
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.satirr.commons.compose.components.ExpressiveFloatingToolbar
import com.satirr.commons.compose.components.ExpressiveMorphingFab
import com.satirr.commons.compose.theme.AppTheme

class ItemsFragment(context: Context, attributeSet: AttributeSet) : MyViewPagerFragment<MyViewPagerFragment.ItemsInnerBinding>(context, attributeSet),
    ItemOperationsListener,
    Breadcrumbs.BreadcrumbsListener {
    private var showHidden = false
    private var lastSearchedText = ""
    private var scrollStates = HashMap<String, Parcelable>()
    private var zoomListener: MyRecyclerView.MyZoomListener? = null

    private var storedItems = ArrayList<ListItem>()
    private var itemsIgnoringSearch = ArrayList<ListItem>()
    private lateinit var binding: ItemsFragmentBinding

    private val isFabExpandedState = mutableStateOf(true)
    private val isFabVisibleState = mutableStateOf(true)
    private val isCreateDocumentState = mutableStateOf(false)
    private val selectedItemsCountState = mutableIntStateOf(0)
    private val totalSelectableCountState = mutableIntStateOf(0)

    override fun onFinishInflate() {
        super.onFinishInflate()
        binding = ItemsFragmentBinding.bind(this)
        innerBinding = ItemsInnerBinding(binding)
    }

    override fun setupFragment(activity: SimpleActivity) {
        if (this.activity == null) {
            this.activity = activity
            binding.apply {
                breadcrumbs.listener = this@ItemsFragment
                itemsSwipeRefresh.setOnRefreshListener { refreshFragment() }

                itemsList.addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
                        if (dy > 4) {
                            isFabExpandedState.value = false
                        } else if (dy < -4) {
                            isFabExpandedState.value = true
                        }
                    }
                })

                setupComposeViews()
            }
        }
    }

    override fun updateIsCreateDocumentIntent(isCreateDocumentIntent: Boolean) {
        super.updateIsCreateDocumentIntent(isCreateDocumentIntent)
        isCreateDocumentState.value = isCreateDocumentIntent
    }

    private fun setupComposeViews() {
        binding.itemsFabCompose.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                AppTheme {
                    val isExpanded by remember { isFabExpandedState }
                    val isVisible by remember { isFabVisibleState }
                    val isCreateDoc by remember { isCreateDocumentState }
                    val selectedCount by remember { selectedItemsCountState }

                    if (isVisible && selectedCount == 0) {
                        ExpressiveMorphingFab(
                            isExpanded = isExpanded,
                            icon = if (isCreateDoc) Icons.Rounded.Check else Icons.Rounded.Add,
                            label = if (isCreateDoc) stringResource(R.string.ok) else stringResource(R.string.new_item),
                            onClick = {
                                if (isCreateDocumentIntent) {
                                    (this@ItemsFragment.activity as MainActivity).createDocumentConfirmed(currentPath)
                                } else {
                                    createNewItem()
                                }
                            }
                        )
                    }
                }
            }
        }

        binding.itemsSelectionToolbarCompose.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                AppTheme {
                    val selectedCount by remember { selectedItemsCountState }

                    ExpressiveFloatingToolbar(
                        isVisible = selectedCount > 0
                    ) {
                        Text(
                            text = "$selectedCount",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )

                        IconButton(
                            onClick = { getRecyclerAdapter()?.executeAction(R.id.cab_select_all) }
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.SelectAll,
                                contentDescription = stringResource(R.string.select_all),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        IconButton(
                            onClick = { getRecyclerAdapter()?.executeAction(R.id.cab_share) }
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Share,
                                contentDescription = stringResource(R.string.share),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        IconButton(
                            onClick = { getRecyclerAdapter()?.executeAction(R.id.cab_copy_to) }
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.ContentCopy,
                                contentDescription = stringResource(R.string.copy),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        IconButton(
                            onClick = { getRecyclerAdapter()?.executeAction(R.id.cab_move_to) }
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.DriveFileMove,
                                contentDescription = stringResource(R.string.move),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        IconButton(
                            onClick = { getRecyclerAdapter()?.executeAction(R.id.cab_delete) }
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.DeleteOutline,
                                contentDescription = stringResource(R.string.delete),
                                tint = MaterialTheme.colorScheme.error
                            )
                        }

                        IconButton(
                            onClick = { finishActMode() }
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Close,
                                contentDescription = stringResource(R.string.cancel),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onResume(textColor: Int) {
        context!!.updateTextColors(this)
        getRecyclerAdapter()?.apply {
            updatePrimaryColor()
            updateTextColor(textColor)
            initDrawables()
        }

        binding.apply {
            val properPrimaryColor = context!!.getProperPrimaryColor()
            itemsFastscroller.updateColors(properPrimaryColor)
            progressBar.setIndicatorColor(properPrimaryColor)
            progressBar.trackColor = properPrimaryColor.adjustAlpha(LOWER_ALPHA)

            if (currentPath != "") {
                breadcrumbs.updateColor(textColor)
            }

            itemsSwipeRefresh.isEnabled = lastSearchedText.isEmpty() && activity?.config?.enablePullToRefresh != false
        }
    }

    override fun setupFontSize() {
        getRecyclerAdapter()?.updateFontSizes()
        if (currentPath != "") {
            binding.breadcrumbs.updateFontSize(context!!.getTextSize(), false)
        }
    }

    override fun setupDateTimeFormat() {
        getRecyclerAdapter()?.updateDateTimeFormat()
    }

    override fun finishActMode() {
        getRecyclerAdapter()?.finishActMode()
        selectedItemsCountState.intValue = 0
    }

    fun openPath(path: String, forceRefresh: Boolean = false) {
        if ((activity as? BaseSimpleActivity)?.isAskingPermissions == true) {
            return
        }

        var realPath = path.trimEnd('/')
        if (realPath.isEmpty()) {
            realPath = "/"
        }

        scrollStates[currentPath] = getScrollState()!!
        currentPath = realPath
        showHidden = context!!.config.shouldShowHidden()
        showProgressBar()
        getItems(currentPath) { originalPath, listItems ->
            if (currentPath != originalPath) {
                return@getItems
            }

            FileDirItem.sorting = context!!.config.getFolderSorting(currentPath)
            listItems.sort()

            if (context!!.config.getFolderViewType(currentPath) == VIEW_TYPE_GRID && listItems.none { it.isSectionTitle }) {
                if (listItems.any { it.mIsDirectory } && listItems.any { !it.mIsDirectory }) {
                    val firstFileIndex = listItems.indexOfFirst { !it.mIsDirectory }
                    if (firstFileIndex != -1) {
                        val sectionTitle = ListItem("", "", false, 0, 0, 0, false, true)
                        listItems.add(firstFileIndex, sectionTitle)
                    }
                }
            }

            itemsIgnoringSearch = listItems
            activity?.runOnUiThread {
                (activity as? MainActivity)?.refreshMenuItems()
                addItems(listItems, forceRefresh)
                if (context != null && currentViewType != context!!.config.getFolderViewType(currentPath)) {
                    setupLayoutManager()
                }
                hideProgressBar()
            }
        }
    }

    private fun addItems(items: ArrayList<ListItem>, forceRefresh: Boolean = false) {
        activity?.runOnUiThread {
            binding.itemsSwipeRefresh.isRefreshing = false
            binding.breadcrumbs.setBreadcrumb(currentPath)
            if (!forceRefresh && items.hashCode() == storedItems.hashCode()) {
                return@runOnUiThread
            }

            storedItems = items
            if (binding.itemsList.adapter == null) {
                binding.breadcrumbs.updateFontSize(context!!.getTextSize(), true)
            }

            ItemsAdapter(activity as SimpleActivity, storedItems, this, binding.itemsList, isPickMultipleIntent, binding.itemsSwipeRefresh) {
                if ((it as? ListItem)?.isSectionTitle == true) {
                    openDirectory(it.mPath)
                    searchClosed()
                } else {
                    itemClicked(it as FileDirItem)
                }
            }.apply {
                setupZoomListener(zoomListener)
                onSelectionChangedListener = { count, total ->
                    selectedItemsCountState.intValue = count
                    totalSelectableCountState.intValue = total
                }
                binding.itemsList.adapter = this
            }

            if (context.areSystemAnimationsEnabled) {
                binding.itemsList.scheduleLayoutAnimation()
            }

            getRecyclerLayoutManager().onRestoreInstanceState(scrollStates[currentPath])
        }
    }

    private fun getScrollState() = getRecyclerLayoutManager().onSaveInstanceState()

    private fun getRecyclerLayoutManager() = (binding.itemsList.layoutManager as MyGridLayoutManager)

    @SuppressLint("NewApi")
    private fun getItems(path: String, callback: (originalPath: String, items: ArrayList<ListItem>) -> Unit) {
        ensureBackgroundThread {
            if (activity?.isDestroyed == false && activity?.isFinishing == false) {
                val config = context!!.config
                if (context.isRestrictedSAFOnlyRoot(path)) {
                    activity?.runOnUiThread { hideProgressBar() }
                    activity?.handleAndroidSAFDialog(path, openInSystemAppAllowed = true) {
                        if (!it) {
                            activity?.toast(R.string.no_storage_permissions)
                            return@handleAndroidSAFDialog
                        }
                        val getProperChildCount = context!!.config.getFolderViewType(currentPath) == VIEW_TYPE_LIST
                        context.getAndroidSAFFileItems(path, context.config.shouldShowHidden(), getProperChildCount) { fileItems ->
                            callback(path, getListItemsFromFileDirItems(fileItems))
                        }
                    }
                } else if (context!!.isPathOnOTG(path) && config.OTGTreeUri.isNotEmpty()) {
                    val getProperFileSize = context!!.config.getFolderSorting(currentPath) and SORT_BY_SIZE != 0
                    context!!.getOTGItems(path, config.shouldShowHidden(), getProperFileSize) {
                        callback(path, getListItemsFromFileDirItems(it))
                    }
                } else if (!config.enableRootAccess || !context!!.isPathOnRoot(path)) {
                    getRegularItemsOf(path, callback)
                } else {
                    RootHelpers(activity!!).getFiles(path, callback)
                }
            }
        }
    }

    private fun getRegularItemsOf(path: String, callback: (originalPath: String, items: ArrayList<ListItem>) -> Unit) {
        val items = ArrayList<ListItem>()
        val files = File(path).listFiles()?.filterNotNull()
        if (context == null || files == null) {
            callback(path, items)
            return
        }

        val isSortingBySize = context!!.config.getFolderSorting(currentPath) and SORT_BY_SIZE != 0
        val getProperChildCount = context!!.config.getFolderViewType(currentPath) == VIEW_TYPE_LIST
        val lastModifieds = context!!.getFolderLastModifieds(path)

        for (file in files) {
            val listItem = getListItemFromFile(file, isSortingBySize, lastModifieds, false)
            if (listItem != null) {
                if (wantedMimeTypes.any { isProperMimeType(it, file.absolutePath, file.isDirectory) }) {
                    items.add(listItem)
                }
            }
        }

        // send out the initial item list asap, get proper child count asynchronously as it can be slow
        callback(path, items)

        if (getProperChildCount) {
            items.filter { it.mIsDirectory }.forEach {
                if (context != null) {
                    val childrenCount = it.getDirectChildrenCount(activity as BaseSimpleActivity, showHidden)
                    if (childrenCount != 0) {
                        activity?.runOnUiThread {
                            getRecyclerAdapter()?.updateChildCount(it.mPath, childrenCount)
                        }
                    }
                }
            }
        }
    }

    private fun getListItemFromFile(file: File, isSortingBySize: Boolean, lastModifieds: HashMap<String, Long>, getProperChildCount: Boolean): ListItem? {
        val curPath = file.absolutePath
        val curName = file.name
        if (!showHidden && curName.startsWith(".")) {
            return null
        }

        var lastModified = lastModifieds.remove(curPath)
        val isDirectory = file.isDirectory
        val children = if (isDirectory && getProperChildCount) file.getDirectChildrenCount(context, showHidden) else 0
        val size = if (isDirectory) {
            if (isSortingBySize) {
                file.getProperSize(showHidden)
            } else {
                0L
            }
        } else {
            file.length()
        }

        if (lastModified == null) {
            lastModified = file.lastModified()
        }

        return ListItem(curPath, curName, isDirectory, children, size, lastModified, false, false)
    }

    private fun getListItemsFromFileDirItems(fileDirItems: ArrayList<FileDirItem>): ArrayList<ListItem> {
        val listItems = ArrayList<ListItem>()
        fileDirItems.forEach {
            val listItem = ListItem(it.path, it.name, it.isDirectory, it.children, it.size, it.modified, false, false)
            if (wantedMimeTypes.any { mimeType -> isProperMimeType(mimeType, it.path, it.isDirectory) }) {
                listItems.add(listItem)
            }
        }
        return listItems
    }

    private fun itemClicked(item: FileDirItem) {
        if (item.isDirectory) {
            openDirectory(item.path)
        } else {
            clickedPath(item.path)
        }
    }

    private fun openDirectory(path: String) {
        (activity as? MainActivity)?.apply {
            openedDirectory()
        }
        openPath(path)
    }

    override fun searchQueryChanged(text: String) {
        lastSearchedText = text
        if (context == null) {
            return
        }

        binding.apply {
            itemsSwipeRefresh.isEnabled = text.isEmpty() && activity?.config?.enablePullToRefresh != false
            when {
                text.isEmpty() -> {
                    itemsFastscroller.beVisible()
                    getRecyclerAdapter()?.updateItems(itemsIgnoringSearch)
                    itemsPlaceholder.beGone()
                    itemsPlaceholder2.beGone()
                    hideProgressBar()
                }

                text.length == 1 -> {
                    itemsFastscroller.beGone()
                    itemsPlaceholder.beVisible()
                    itemsPlaceholder2.beVisible()
                    hideProgressBar()
                }

                else -> {
                    showProgressBar()
                    ensureBackgroundThread {
                        val files = searchFiles(text, currentPath)
                        files.sortBy { it.getParentPath() }

                        if (lastSearchedText != text) {
                            return@ensureBackgroundThread
                        }

                        val listItems = ArrayList<ListItem>()

                        var previousParent = ""
                        files.forEach {
                            val parent = it.mPath.getParentPath()
                            if (!it.isDirectory && parent != previousParent && context != null) {
                                val sectionTitle = ListItem(parent, context!!.humanizePath(parent), false, 0, 0, 0, true, false)
                                listItems.add(sectionTitle)
                                previousParent = parent
                            }

                            if (it.isDirectory) {
                                val sectionTitle = ListItem(it.path, context!!.humanizePath(it.path), true, 0, 0, 0, true, false)
                                listItems.add(sectionTitle)
                                previousParent = parent
                            }

                            if (!it.isDirectory) {
                                listItems.add(it)
                            }
                        }

                        activity?.runOnUiThread {
                            getRecyclerAdapter()?.updateItems(listItems, text)
                            itemsFastscroller.beVisibleIf(listItems.isNotEmpty())
                            itemsPlaceholder.beVisibleIf(listItems.isEmpty())
                            itemsPlaceholder2.beGone()
                            hideProgressBar()
                        }
                    }
                }
            }
        }
    }

    private fun searchFiles(text: String, path: String): ArrayList<ListItem> {
        val files = ArrayList<ListItem>()
        if (context == null) {
            return files
        }

        val normalizedText = text.normalizeString()
        val sorting = context!!.config.getFolderSorting(path)
        FileDirItem.sorting = context!!.config.getFolderSorting(currentPath)
        val isSortingBySize = sorting and SORT_BY_SIZE != 0
        File(path).listFiles()?.sortedBy { it.isDirectory }?.forEach {
            if (!showHidden && it.isHidden) {
                return@forEach
            }

            if (it.isDirectory) {
                if (it.name.normalizeString().contains(normalizedText, true)) {
                    val fileDirItem = getListItemFromFile(it, isSortingBySize, HashMap(), false)
                    if (fileDirItem != null) {
                        files.add(fileDirItem)
                    }
                }

                files.addAll(searchFiles(text, it.absolutePath))
            } else {
                if (it.name.normalizeString().contains(normalizedText, true)) {
                    val fileDirItem = getListItemFromFile(it, isSortingBySize, HashMap(), false)
                    if (fileDirItem != null) {
                        files.add(fileDirItem)
                    }
                }
            }
        }
        return files
    }

    private fun searchClosed() {
        binding.apply {
            lastSearchedText = ""
            itemsSwipeRefresh.isEnabled = activity?.config?.enablePullToRefresh != false
            itemsFastscroller.beVisible()
            itemsPlaceholder.beGone()
            itemsPlaceholder2.beGone()
            hideProgressBar()
        }
    }

    private fun createNewItem() {
        CreateNewItemDialog(activity as SimpleActivity, currentPath) {
            if (it) {
                refreshFragment()
            } else {
                activity?.toast(R.string.unknown_error_occurred)
            }
        }
    }

    private fun getRecyclerAdapter() = binding.itemsList.adapter as? ItemsAdapter

    private fun setupLayoutManager() {
        if (context!!.config.getFolderViewType(currentPath) == VIEW_TYPE_GRID) {
            currentViewType = VIEW_TYPE_GRID
            setupGridLayoutManager()
        } else {
            currentViewType = VIEW_TYPE_LIST
            setupListLayoutManager()
        }

        binding.itemsList.adapter = null
        initZoomListener()
        addItems(storedItems, true)
    }

    private fun setupGridLayoutManager() {
        val layoutManager = binding.itemsList.layoutManager as MyGridLayoutManager
        layoutManager.spanCount = context?.config?.fileColumnCnt ?: 3

        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (getRecyclerAdapter()?.isASectionTitle(position) == true || getRecyclerAdapter()?.isGridTypeDivider(position) == true) {
                    layoutManager.spanCount
                } else {
                    1
                }
            }
        }
    }

    private fun setupListLayoutManager() {
        val layoutManager = binding.itemsList.layoutManager as MyGridLayoutManager
        layoutManager.spanCount = 1
        zoomListener = null
    }

    private fun initZoomListener() {
        if (context?.config?.getFolderViewType(currentPath) == VIEW_TYPE_GRID) {
            val layoutManager = binding.itemsList.layoutManager as MyGridLayoutManager
            zoomListener = object : MyRecyclerView.MyZoomListener {
                override fun zoomIn() {
                    if (layoutManager.spanCount > 1) {
                        reduceColumnCount()
                        getRecyclerAdapter()?.finishActMode()
                    }
                }

                override fun zoomOut() {
                    if (layoutManager.spanCount < MAX_COLUMN_COUNT) {
                        increaseColumnCount()
                        getRecyclerAdapter()?.finishActMode()
                    }
                }
            }
        } else {
            zoomListener = null
        }
    }

    private fun increaseColumnCount() {
        if (currentViewType == VIEW_TYPE_GRID) {
            context!!.config.fileColumnCnt += 1
            (activity as? MainActivity)?.updateFragmentColumnCounts()
        }
    }

    private fun reduceColumnCount() {
        if (currentViewType == VIEW_TYPE_GRID) {
            context!!.config.fileColumnCnt -= 1
            (activity as? MainActivity)?.updateFragmentColumnCounts()
        }
    }

    override fun columnCountChanged() {
        (binding.itemsList.layoutManager as MyGridLayoutManager).spanCount = context!!.config.fileColumnCnt
        (activity as? MainActivity)?.refreshMenuItems()
        getRecyclerAdapter()?.apply {
            notifyItemRangeChanged(0, listItems.size)
        }
    }

    fun showProgressBar() {
        binding.progressBar.show()
    }

    private fun hideProgressBar() {
        binding.progressBar.hide()
    }

    fun getBreadcrumbs() = binding.breadcrumbs

    override fun toggleFilenameVisibility() {
        getRecyclerAdapter()?.updateDisplayFilenamesInGrid()
    }

    override fun breadcrumbClicked(id: Int) {
        if (id == 0) {
            StoragePickerDialog(activity as SimpleActivity, currentPath, context!!.config.enableRootAccess, true) {
                getRecyclerAdapter()?.finishActMode()
                openPath(it)
            }
        } else {
            val item = binding.breadcrumbs.getItem(id)
            openPath(item.path)
        }
    }

    override fun refreshFragment() {
        openPath(currentPath)
    }

    override fun deleteFiles(files: ArrayList<FileDirItem>) {
        val hasFolder = files.any { it.isDirectory }
        handleFileDeleting(files, hasFolder)
    }

    override fun selectedPaths(paths: ArrayList<String>) {
        (activity as MainActivity).pickedPaths(paths)
    }
}
