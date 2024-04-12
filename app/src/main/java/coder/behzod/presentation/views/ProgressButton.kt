package coder.behzod.presentation.views

import androidx.compose.foundation.BorderStroke
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coder.behzod.presentation.theme.fontAmidoneGrotesk
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSButtonState
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSButtonType
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSJetPackComposeProgressButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ProgressButton(
    content: () -> Unit,
    text: String,
    color: Color,
    assetColor: Color
) {
    val coroutineScope = rememberCoroutineScope()
    val buttonState = remember { mutableStateOf(SSButtonState.IDLE) }
    SSJetPackComposeProgressButton(
        type = SSButtonType.CLOCK,
        width = 300.dp,
        height = 50.dp,
        onClick = {
            buttonState.value = SSButtonState.LOADING
            coroutineScope.launch {
                delay(1500L)
                buttonState.value = SSButtonState.SUCCESS
                content()
            }
        },
        buttonBorderStroke = BorderStroke(1.dp, assetColor),
        assetColor = assetColor,
        colors =  ButtonDefaults.buttonColors(
            color
        ),
        buttonState = buttonState.value,
        text = text,
        fontFamily = FontFamily(fontAmidoneGrotesk),
        fontSize = 18.sp,
    )
}