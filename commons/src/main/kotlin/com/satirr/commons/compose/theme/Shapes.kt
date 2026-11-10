package com.satirr.commons.compose.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

private const val PERCENT_PILL = 50

val Shapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(32.dp),
)

object ExpressiveShapes {
    val Pill = RoundedCornerShape(percent = PERCENT_PILL)
    val Circle = CircleShape
    val Container = RoundedCornerShape(28.dp)
    val Dialog = RoundedCornerShape(32.dp)
    val Card = RoundedCornerShape(24.dp)
    val Chip = RoundedCornerShape(12.dp)
    val Fab = RoundedCornerShape(20.dp)

    val AsymmetricStart = RoundedCornerShape(
        topStart = 28.dp,
        bottomStart = 28.dp,
        topEnd = 8.dp,
        bottomEnd = 8.dp
    )
    val AsymmetricEnd = RoundedCornerShape(
        topStart = 8.dp,
        bottomStart = 8.dp,
        topEnd = 28.dp,
        bottomEnd = 28.dp
    )
}
