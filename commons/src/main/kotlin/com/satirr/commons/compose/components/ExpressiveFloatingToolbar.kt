package com.satirr.commons.compose.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.satirr.commons.compose.theme.ExpressiveMotion
import com.satirr.commons.compose.theme.ExpressiveShapes

private val ToolbarElevation = 8.dp
private val ToolbarHorizontalMargin = 16.dp
private val ToolbarVerticalMargin = 24.dp
private val ToolbarContentHorizontalPadding = 12.dp
private val ToolbarContentVerticalPadding = 8.dp
private val ToolbarItemSpacing = 8.dp

@Composable
fun ExpressiveFloatingToolbar(
    isVisible: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(
            animationSpec = ExpressiveMotion.spatialSpring(),
            initialOffsetY = { it }
        ) + fadeIn(animationSpec = ExpressiveMotion.spatialSpring()),
        exit = slideOutVertically(
            animationSpec = ExpressiveMotion.spatialSpring(),
            targetOffsetY = { it }
        ) + fadeOut(animationSpec = ExpressiveMotion.fastSpring()),
        modifier = modifier
    ) {
        Surface(
            modifier = Modifier
                .padding(horizontal = ToolbarHorizontalMargin, vertical = ToolbarVerticalMargin)
                .clip(ExpressiveShapes.Pill),
            shape = ExpressiveShapes.Pill,
            color = MaterialTheme.colorScheme.surfaceContainerHighest,
            shadowElevation = ToolbarElevation
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = ToolbarContentHorizontalPadding, vertical = ToolbarContentVerticalPadding),
                horizontalArrangement = Arrangement.spacedBy(ToolbarItemSpacing),
                verticalAlignment = Alignment.CenterVertically,
                content = content
            )
        }
    }
}
