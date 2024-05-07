package coder.behzod.presentation.views

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import coder.behzod.R
import coder.behzod.presentation.theme.fontAmidoneGrotesk

@Composable
fun ActionSnackbar(
    modifier: Modifier = Modifier,
    themeColor: Color,
    fontColor: Color,
    onClick: () -> Unit
) {
    Snackbar(
        content = {
            Text(
                text = stringResource(R.string.note_deleted),
                color = themeColor,
                fontSize = 18.sp,
                fontFamily = FontFamily(fontAmidoneGrotesk)
            )
        },
        action = {
            Button(
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = themeColor
                ),
                onClick = {
                onClick()
            }) {
                Text(
                    text = stringResource(R.string.undo),
                    color = fontColor,
                    fontSize = 18.sp,
                    fontFamily = FontFamily(fontAmidoneGrotesk)
                )
            }
        },
        containerColor = fontColor
    )
}

@Preview
@Composable
private fun PreviewAction() {
    ActionSnackbar(themeColor = Color.Black, fontColor = Color.White) {

    }
}