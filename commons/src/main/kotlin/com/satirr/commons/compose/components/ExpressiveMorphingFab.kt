package com.satirr.commons.compose.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.satirr.commons.compose.theme.ExpressiveMotion
import com.satirr.commons.compose.theme.ExpressiveShapes

private val FabDefaultElevation = 4.dp
private val FabPressedElevation = 8.dp
private val FabHorizontalPadding = 16.dp
private val FabVerticalPadding = 12.dp
private val FabIconSize = 24.dp
private val FabSpacing = 8.dp

@Composable
fun ExpressiveMorphingFab(
    isExpanded: Boolean,
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onClick,
        shape = if (isExpanded) ExpressiveShapes.Pill else ExpressiveShapes.Fab,
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = FabDefaultElevation,
            pressedElevation = FabPressedElevation
        ),
        modifier = modifier.animateContentSize(animationSpec = ExpressiveMotion.spatialSpring())
    ) {
        Row(
            modifier = Modifier.padding(horizontal = FabHorizontalPadding, vertical = FabVerticalPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(FabIconSize)
            )
            if (isExpanded) {
                Spacer(modifier = Modifier.width(FabSpacing))
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}
