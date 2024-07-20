package coder.behzod.presentation.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import coder.behzod.R
import coder.behzod.presentation.theme.fontAmidoneGrotesk
import coder.behzod.presentation.theme.red

@Composable
fun DialogViewInstance(
    fontSize:Int,
    isDialogVisible: Boolean,
    viewIndex:(Int)->Unit,
    dismissRequest:()->Unit
) {
    val showDialog = remember { mutableStateOf(isDialogVisible) }
    if (showDialog.value) {

        AlertDialog(
            onDismissRequest = {
                showDialog.value = false
                dismissRequest()
            },
            confirmButton = {

            },
            modifier = Modifier
                .background(Color.Transparent)
                .border(
                    width = 0.5.dp,
                    color = Color.White,
                    shape = RoundedCornerShape(10.dp)
                ),
            containerColor = Color.Transparent,
            tonalElevation = 10.dp,
            properties = DialogProperties(
                dismissOnClickOutside = false,
                dismissOnBackPress = false,
            ),
            title = {
                    Text(
                        text = stringResource(id = R.string.select_view),
                        color = Color.White,
                        fontSize = fontSize.plus(5).sp,
                        fontFamily = FontFamily(fontAmidoneGrotesk)
                    )
            },
            dismissButton = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ){
                    Button(
                        modifier = Modifier
                            .width(200.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = red
                        ),
                        onClick = {
                            showDialog.value = false
                            dismissRequest()
                        }) {
                        Text(
                            text = stringResource(id = R.string.cancel),
                            color = Color.White,
                            fontSize = fontSize.sp,
                            fontFamily = FontFamily(fontAmidoneGrotesk)
                        )
                    }
                }
            },
            icon = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            viewIndex(1)
                        }
                    ) {
                        Icon(
                            painterResource(
                                id = R.drawable.ic_grid
                            ),
                            modifier = Modifier
                                .size(35.dp),
                            contentDescription = "gridView",
                            tint = Color.White
                        )
                    }
                    IconButton(
                        onClick = {
                            viewIndex(0)
                        }
                    ) {
                        Icon(
                            painterResource(
                                id = R.drawable.ic_list
                            ),
                            modifier = Modifier
                                .size(35.dp),
                            contentDescription = "listView",
                            tint = Color.White
                        )
                    }
                }
            }
        )
    }
}