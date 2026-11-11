package com.satirr.commons.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.satirr.commons.compose.theme.ExpressiveShapes

private val DefaultProgressHeight = 8.dp
private val CircularStrokeWidth = 4.dp
private val LoadingContainerPadding = 24.dp
private val LoadingIndicatorSize = 48.dp
private val LoadingElevation = 6.dp

@Composable
fun ExpressiveLinearProgressIndicator(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    trackColor: Color = MaterialTheme.colorScheme.surfaceContainerHighest,
    strokeCap: StrokeCap = StrokeCap.Round,
    height: Dp = DefaultProgressHeight
) {
    LinearProgressIndicator(
        modifier = modifier
            .height(height)
            .clip(ExpressiveShapes.Pill),
        color = color,
        trackColor = trackColor,
        strokeCap = strokeCap
    )
}

@Composable
fun ExpressiveLinearProgressIndicator(
    progress: () -> Float,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    trackColor: Color = MaterialTheme.colorScheme.surfaceContainerHighest,
    strokeCap: StrokeCap = StrokeCap.Round,
    height: Dp = DefaultProgressHeight
) {
    LinearProgressIndicator(
        progress = progress,
        modifier = modifier
            .height(height)
            .clip(ExpressiveShapes.Pill),
        color = color,
        trackColor = trackColor,
        strokeCap = strokeCap
    )
}

@Composable
fun ExpressiveCircularProgressIndicator(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    trackColor: Color = MaterialTheme.colorScheme.surfaceContainerHighest,
    strokeWidth: Dp = CircularStrokeWidth,
    strokeCap: StrokeCap = StrokeCap.Round
) {
    CircularProgressIndicator(
        modifier = modifier,
        color = color,
        trackColor = trackColor,
        strokeWidth = strokeWidth,
        strokeCap = strokeCap
    )
}

@Composable
fun ExpressiveCircularProgressIndicator(
    progress: () -> Float,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    trackColor: Color = MaterialTheme.colorScheme.surfaceContainerHighest,
    strokeWidth: Dp = CircularStrokeWidth,
    strokeCap: StrokeCap = StrokeCap.Round
) {
    CircularProgressIndicator(
        progress = progress,
        modifier = modifier,
        color = color,
        trackColor = trackColor,
        strokeWidth = strokeWidth,
        strokeCap = strokeCap
    )
}

@Composable
fun ExpressiveLoadingIndicator(
    modifier: Modifier = Modifier,
    message: String? = null,
    color: Color = MaterialTheme.colorScheme.primary
) {
    Surface(
        modifier = modifier,
        shape = ExpressiveShapes.Container,
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        shadowElevation = LoadingElevation
    ) {
        Column(
            modifier = Modifier.padding(LoadingContainerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(LoadingIndicatorSize),
                color = color,
                strokeWidth = CircularStrokeWidth,
                strokeCap = StrokeCap.Round
            )
            if (message != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
