package com.satirr.range.activities

import android.graphics.Paint
import android.os.Bundle
import com.satirr.commons.dialogs.FilePickerDialog
import com.satirr.commons.extensions.beVisibleIf
import com.satirr.commons.extensions.getProperPrimaryColor
import com.satirr.commons.extensions.getProperTextColor
import com.satirr.commons.extensions.viewBinding
import com.satirr.commons.helpers.NavigationIcon
import com.satirr.commons.interfaces.RefreshRecyclerViewListener
import com.satirr.range.R
import com.satirr.range.adapters.ManageFavoritesAdapter
import com.satirr.range.databinding.ActivityFavoritesBinding
import com.satirr.range.extensions.config

class FavoritesActivity : SimpleActivity(), RefreshRecyclerViewListener {
    private val binding by viewBinding(ActivityFavoritesBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupOptionsMenu()
        updateFavorites()
        binding.apply {
            setupEdgeToEdge(padBottomSystem = listOf(manageFavoritesList))
            setupMaterialScrollListener(binding.manageFavoritesList, binding.manageFavoritesAppbar)
        }
    }

    override fun onResume() {
        super.onResume()
        setupTopAppBar(binding.manageFavoritesAppbar, NavigationIcon.Arrow)
    }

    private fun setupOptionsMenu() {
        binding.manageFavoritesToolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.add_favorite -> addFavorite()
                else -> return@setOnMenuItemClickListener false
            }
            return@setOnMenuItemClickListener true
        }
    }

    private fun updateFavorites() {
        binding.apply {
            val favorites = ArrayList<String>()
            config.favorites.mapTo(favorites) { it }
            manageFavoritesPlaceholder.beVisibleIf(favorites.isEmpty())
            manageFavoritesPlaceholder.setTextColor(getProperTextColor())

            manageFavoritesPlaceholder2.apply {
                paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
                beVisibleIf(favorites.isEmpty())
                setTextColor(getProperPrimaryColor())
                setOnClickListener {
                    addFavorite()
                }
            }

            ManageFavoritesAdapter(this@FavoritesActivity, favorites, this@FavoritesActivity, manageFavoritesList) { }.apply {
                manageFavoritesList.adapter = this
            }
        }
    }

    override fun refreshItems() {
        updateFavorites()
    }

    private fun addFavorite() {
        FilePickerDialog(this, pickFile = false, showHidden = config.shouldShowHidden(), canAddShowHiddenButton = true) {
            config.addFavorite(it)
            updateFavorites()
        }
    }
}
