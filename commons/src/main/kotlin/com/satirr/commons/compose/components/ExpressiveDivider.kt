package com.satirr.commons.compose.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val DefaultDividerThickness = 1.dp

@Composable
fun ExpressiveHorizontalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = DefaultDividerThickness,
    color: Color = MaterialTheme.colorScheme.outlineVariant,
    startIndent: Dp = 0.dp,
    endIndent: Dp = 0.dp
) {
    HorizontalDivider(
        modifier = modifier.padding(start = startIndent, end = endIndent),
        thickness = thickness,
        color = color
    )
}

@Composable
fun ExpressiveVerticalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = DefaultDividerThickness,
    color: Color = MaterialTheme.colorScheme.outlineVariant,
    topIndent: Dp = 0.dp,
    bottomIndent: Dp = 0.dp
) {
    VerticalDivider(
        modifier = modifier.padding(top = topIndent, bottom = bottomIndent),
        thickness = thickness,
        color = color
    )
}
