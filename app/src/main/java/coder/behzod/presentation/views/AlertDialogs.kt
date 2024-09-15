package coder.behzod.presentation.views

import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.DialogProperties

@Composable
fun AlertDialogs(
    dismissButton: @Composable () -> Unit?,
    confirmButton: @Composable () -> Unit?,
    onDismissRequest:()->Unit,
    title: @Composable () -> Unit,
    content: @Composable () -> Unit,
    icon: @Composable () -> Unit,
) {
    val dialogProperties = DialogProperties()
    AlertDialog(
        containerColor = Color.DarkGray,
        title = {
            title()
        },
        text = {
            content()
        },
        icon = {
            icon()
        }, dismissButton = {
            dismissButton()
        },
        onDismissRequest = {
            if (dialogProperties.dismissOnBackPress || dialogProperties.dismissOnClickOutside) {
                onDismissRequest()
            }
        },
        confirmButton = {
            confirmButton()
        }
    )
}