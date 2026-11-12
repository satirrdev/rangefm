package com.satirr.commons.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.satirr.commons.compose.theme.ExpressiveShapes

private val DialogIconSize = 28.dp
private val DialogContentPadding = 24.dp
private val DialogItemSpacing = 16.dp
private val DialogButtonSpacing = 8.dp
private val DialogElevation = 6.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpressiveAlertDialog(
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    dismissButton: (@Composable () -> Unit)? = null,
    icon: ImageVector? = null,
    title: String? = null,
    text: String? = null,
    properties: DialogProperties = DialogProperties(),
    content: (@Composable () -> Unit)? = null
) {
    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        properties = properties
    ) {
        Surface(
            shape = ExpressiveShapes.Dialog,
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            tonalElevation = DialogElevation,
            shadowElevation = DialogElevation
        ) {
            Column(
                modifier = Modifier.padding(DialogContentPadding)
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(DialogIconSize)
                            .align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(DialogItemSpacing))
                }

                if (title != null) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = if (icon != null) TextAlign.Center else TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(DialogItemSpacing))
                }

                if (text != null) {
                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(DialogItemSpacing))
                }

                if (content != null) {
                    content()
                    Spacer(modifier = Modifier.height(DialogItemSpacing))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(DialogButtonSpacing, Alignment.End),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (dismissButton != null) {
                        dismissButton()
                    }
                    confirmButton()
                }
            }
        }
    }
}
