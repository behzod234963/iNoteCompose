package coder.behzod.presentation.views

import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import coder.behzod.presentation.theme.fontAmidoneGrotesk
import coder.behzod.presentation.theme.green
import coder.behzod.presentation.theme.red

@Composable
fun AlertDialogInstance(
    fontSize: Int,
    icon: ImageVector? = null,
    title: String?,
    text: String,
    showDialog: Boolean = false,
    confirmButtonText: String,
    confirmButton: () -> Unit,
    dismissButtonText: String,
    dismissButton: () -> Unit,
    onDismissRequest: () -> Unit
) {
    val isDialogOn = remember { mutableStateOf(showDialog) }
    val dialogProperties = DialogProperties()
    if (isDialogOn.value) {
        AlertDialog(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .border(
                    width = 0.5.dp,
                    color = Color.White,
                    shape = RoundedCornerShape(10.dp)
                ),
            containerColor = Color.Transparent,
            tonalElevation = 10.dp,
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            ),
            icon = {

                icon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = "example Icon",
                        tint = Color.White
                    )
                }
            },
            title = {
                if (title != null) {
                    Text(
                        text = title,
                        color = Color.White,
                        fontSize = fontSize.plus(5).sp,
                        fontFamily = FontFamily(fontAmidoneGrotesk)
                    )
                }
            },
            text = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = text,
                        color = Color.White,
                        fontSize = fontSize.sp,
                        fontFamily = FontFamily(fontAmidoneGrotesk)
                    )
                }
            },
            onDismissRequest = {
                isDialogOn.value = false
                onDismissRequest()
                if (dialogProperties.dismissOnBackPress || dialogProperties.dismissOnClickOutside){
                    isDialogOn.value = false
                    onDismissRequest()
                }
            }, confirmButton = {
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        modifier = Modifier
                            .width(110.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = green
                        ),
                        onClick = {
                            confirmButton()
                        }) {
                        Text(
                            text = confirmButtonText,
                            color = Color.White,
                            fontSize = fontSize.sp,
                            fontFamily = FontFamily(fontAmidoneGrotesk)
                        )
                    }
                }
            },
            dismissButton = {
                Row(
                    horizontalArrangement = Arrangement.Start
                ) {
                    Button(
                        modifier = Modifier
                            .width(150.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = red
                        ),
                        onClick = {
                            dismissButton()
                        }) {
                        Text(
                            text = dismissButtonText,
                            color = Color.White,
                            fontSize = fontSize.sp,
                            fontFamily = FontFamily(fontAmidoneGrotesk)
                        )
                    }
                }
            }
        )
    }
}